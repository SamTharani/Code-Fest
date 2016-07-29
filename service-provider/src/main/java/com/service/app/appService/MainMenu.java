package com.service.app.appService;

import com.service.app.dao.CustomerDao;
import com.service.app.dao.ItemDao;
import com.service.app.models.Customer;
import com.service.app.util.Messages;
import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.StatusCodes;
import hms.kite.samples.api.ussd.MoUssdListener;
import hms.kite.samples.api.ussd.UssdRequestSender;
import hms.kite.samples.api.ussd.messages.MoUssdReq;
import hms.kite.samples.api.ussd.messages.MtUssdReq;
import hms.kite.samples.api.ussd.messages.MtUssdResp;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.MissingResourceException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMenu implements MoUssdListener {

    private final static Logger LOGGER = Logger.getLogger(MainMenu.class.getName());

    private static final String EXIT_SERVICE_CODE = "000";
    private static final String BACK_SERVICE_CODE = "999";
    private static final String USSD_OP_MO_INIT = "mo-init";
    private static final String USSD_OP_MT_CONT = "mt-cont";
    private static final String USSD_OP_MT_FIN = "mt-fin";
    private static final String NEXT_SERVICE_CODE = "992";
    private static final String MAIN_SERVICE_CODE = "994";
    public static int level = 0;
    int count_level0 = 1;
    int count_level1 = 1;
    String init_key = "0";
    int pinCode;
    String donorName = "";
    String serviceDuration = "";
    String bloodType = "";
    String neededTime = "";
    String location = "";
    String display = "";
    String bloodBankName = "";
    String bloodBankLocation = "";
    private List<String> menuState = new ArrayList<String>();
    // service to send the request
    private UssdRequestSender ussdMtSender;

    ItemDao itemDao;

    @Override
    public void init() {
        // create and initialize service
        try {
            ussdMtSender = new UssdRequestSender(new URL(Messages.getMessage("sdp.server.url")));

        } catch (MalformedURLException e) {
            LOGGER.log(Level.INFO, "Unexpected error occurred", e);
        }
    }

    @Override
    public void onReceivedUssd(final MoUssdReq moUssdReq) {
        try {
            // start processing request
            processRequest(moUssdReq);
        } catch (SdpException e) {
            LOGGER.log(Level.INFO, "Unexpected error occurred", e);
        }
    }


    private void processRequest(final MoUssdReq moUssdReq) throws SdpException {
        // exit request - session destroy
        if (moUssdReq.getMessage().equals(EXIT_SERVICE_CODE)) {
            terminateSession(moUssdReq);
            return;// completed work and return
        }

        // back button handling
        if (moUssdReq.getMessage().equals(BACK_SERVICE_CODE)) {
            backButtonHandle(moUssdReq);
            return;// completed work and return
        }

        // get current service code
        String serviceCode;
        if (USSD_OP_MO_INIT.equals(moUssdReq.getUssdOperation())) {
            serviceCode = "0";
            level = 0;
            clearMenuState();

        } else {
            if (moUssdReq.getMessage().equals(NEXT_SERVICE_CODE)) {
                if (level == 0) {
                    count_level0 += 1;
                } else if (level == 1) {
                    count_level1 += 1;
                }
                serviceCode = getServiceCode(moUssdReq);
            } else {
                serviceCode = getServiceCode(moUssdReq);
                level = level + 1;
            }
        }
        // create request to display user
        final MtUssdReq request = createRequest(moUssdReq, buildMenuContent(serviceCode), USSD_OP_MT_CONT);
        sendRequest(request);
        // record menu state
        menuState.add(serviceCode);
    }


    private MtUssdReq createRequest(final MoUssdReq moUssdReq, final String menuContent, final String ussdOperation) {
        final MtUssdReq request = new MtUssdReq();
        request.setApplicationId(moUssdReq.getApplicationId());
        request.setEncoding(moUssdReq.getEncoding());
        request.setMessage(menuContent);
        request.setPassword(Messages.getMessage(moUssdReq.getApplicationId()));
        request.setSessionId(moUssdReq.getSessionId());
        request.setUssdOperation(Messages.getMessage("operation"));
        request.setVersion(moUssdReq.getVersion());
        request.setDestinationAddress(moUssdReq.getSourceAddress());
        return request;
    }

    private String getText(final String key) {
        String outputString = "";

        if (level == 0) {
            outputString = "Welcome to SERVICE PROVIDER \n1.Shop owners\n2.Service stations\n3.Service request\n000.Exit";
            System.out.println("level 0 :" + level);
        }
        if (level > 0) {
            if (level == 1) {
                init_key = key;
                if ("1".equals(init_key)) {
                    //outputString = getCategories(key);
                    // TODO: for shop owners
                } else if ("2".equals(init_key)) {
                    //outputString = getBlood_Donar(key);
                    // TODO: for service stations
                } else if("3".equals(init_key)) {
                    outputString = getCategories(key);
                }
                else outputString = outputString + "Invalid Selection" + "\n999:Back" + key;
            }
        }
        return outputString;
    }

    private String getCategories(final String key) {
        String outputString = "";
        if (level == 1) {
            outputString = outputString + "Please enter your pin code:";

        } else if (level == 2) {
            pinCode = Integer.parseInt(key);

            CustomerDao customerDao = new CustomerDao();
            Customer customer = new Customer();
            List t = customerDao.validPinCode(pinCode);

            if (!t.isEmpty()) {
                outputString = outputString + "Select an item you want service: \n 1. Car \n 2. Mobile \n 2. Laptop \n 2. TV \n999:Back";
            } else {
                outputString = outputString + "Invalid pin code! Try again\n999:Back\n000:Exit";
            }
        } else if (level == 3) {
            outputString = outputString + "Where you want to get service: \n 1. Shop \n 2. Nearest service station \n999:Back";
            if ("1".equals(init_key)) {
                outputString = getServiceDuration(key);
            } else if ("2".equals(init_key)) {
                outputString = "Enter your location to get nearest service station\n" +
                        "999:Back\n" +
                        "000:Exit";
            } else outputString = outputString + "Invalid Selection" + "\n999:Back" + key;
        }
        return outputString;

    }

    public String getServiceDuration(String key){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String outputString = "";
        try {
            Date dueDate = sdf.parse("2015-10-10");
            Date currentDate = sdf.parse("2016-10-10");
            Date lessCurrentDate = sdf.parse("2015-05-10");
            String messageForFree = "\"You have free service until 2015-10-10 Thank you \\n\" +\n" +
                    "                            \"999:Back\\n\" +\n" +
                    "                            \"000:Exit\"";
            String messageForPackage ="Your free period time is over you can use our service packages Thank you\n" +
                    "999:Back\n" +
                    "000:Exit";

            if (Integer.parseInt(key) == 1) {
                if(dueDate.compareTo(currentDate) <= 0) {
                   outputString = outputString + messageForFree;
                } else {
                   outputString = outputString + messageForPackage;
                }
            }
            if (Integer.parseInt(key) == 2) {
                if(dueDate.compareTo(currentDate) <= 0) {
                    outputString = outputString + messageForFree;
                } else {
                    outputString = outputString + messageForPackage;
                }
            }
            if (Integer.parseInt(key) == 3) {
                if(dueDate.compareTo(currentDate) <= 0) {
                    outputString = outputString + messageForFree;
                } else {
                    outputString = outputString + messageForPackage;
                }
            }
            if (Integer.parseInt(key) == 4) {
                if(dueDate.compareTo(currentDate) <= 0) {
                    outputString = outputString + messageForFree;
                } else {
                    outputString = outputString + messageForPackage;
                }
            } else outputString = "Invalid selection";

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputString;
    }

    public String getbloodtype(String key) {
        if (Integer.parseInt(key) == 1) {
            return "+";
        }
        if (Integer.parseInt(key) == 2) {
            return "-";
        } else return "Invalid selection";
    }

    private MtUssdResp sendRequest(final MtUssdReq request) throws SdpException {
        // sending request to service
        MtUssdResp response = null;
        try {
            response = ussdMtSender.sendUssdRequest(request);
        } catch (SdpException e) {
            LOGGER.log(Level.INFO, "Unable to send request", e);
            throw e;
        }

        // response status
        String statusCode = response.getStatusCode();
        String statusDetails = response.getStatusDetail();
        if (StatusCodes.SuccessK.equals(statusCode)) {
            LOGGER.info("MT USSD message successfully sent");
        } else {
            LOGGER.info("MT USSD message sending failed with status code [" + statusCode + "] " + statusDetails);
        }
        return response;
    }

    /**
     * Clear history list
     */
    private void clearMenuState() {
        LOGGER.info("clear history List");
        menuState.clear();
        //level=0;
    }

    /**
     * Terminate session
     *
     * @param moUssdReq
     * @throws SdpException
     */
    private void terminateSession(final MoUssdReq moUssdReq) throws SdpException {
        final MtUssdReq request = createRequest(moUssdReq, "", USSD_OP_MT_FIN);
        sendRequest(request);
    }


    private void backButtonHandle(final MoUssdReq moUssdReq) throws SdpException {
        String lastMenuVisited = "0";

        // remove last menu when back
        if (menuState.size() > 0) {
            menuState.remove(menuState.size() - 1);
            lastMenuVisited = menuState.get(menuState.size() - 1);
            level = level - 1;
        }

        // create request and send
        final MtUssdReq request = createRequest(moUssdReq, buildMenuContent(lastMenuVisited), USSD_OP_MT_CONT);

        sendRequest(request);

        // clear menu status
        if ("0".equals(lastMenuVisited)) {
            clearMenuState();
            // add 0 to menu state ,finally its in main menu
            menuState.add((String) "0");
        }

    }


    private String getServiceCode(final MoUssdReq moUssdReq) {
        //byte serviceCode = 0;
        String serviceCode = "0";
        try {
            //serviceCode = Byte.parseByte(moUssdReq.getMessage());
            serviceCode = moUssdReq.getMessage();
        } catch (NumberFormatException e) {
            return serviceCode;
        }
        // create service codes for child menus based on the main menu codes
        return serviceCode;
    }

    private String buildMenuContent(final String selection) {
        String menuContent;
        try {
            // build menu contents           
            menuContent = getText(selection);
        } catch (MissingResourceException e) {
            // back to main menu
            menuContent = getText((String) "0");
        }
        return menuContent;
    }


}
