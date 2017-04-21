package com.pixieproto.bluephish.pixieproto;

/**
 * Created by nadunoorup on 4/18/17.
 */

interface IntentHandlerListener {
    public void processHandlerAfterPermission();
}

public class IntentHandler implements IntentHandlerListener {
    private String contextType = null;
    private String intentType = null;
    private String extractedVal = null;

    public IntentHandler(String contextType, String intentType, String val) {
        this.contextType = contextType;
        this.intentType = intentType;
        this.extractedVal = val;
    }

    public void handleRequest() {
        // copy to clipboard
        if (intentType.equals(Constants.CLIPBOARD)) {
            IntentUtils.copyToClipboard(ContextList.me, extractedVal);

            return;
        }

        switch (contextType) {
            case Constants.EMAIL: {
                switch (intentType) {
                    case Constants.MAIL: {
                        IntentUtils.composeEmail(ContextList.me, null, extractedVal);
                    }
                    break;
                    case Constants.SAVE_TO_CONTACTS: {
                        IntentUtils.saveEmail(ContextList.me, extractedVal);
                    }
                    break;
                    case Constants.SHARE: {
                        IntentUtils.shareInfo(ContextList.me, extractedVal, Constants.EMAIL);
                    }
                    break;
                }
            }
            break;
            case Constants.PHONENUMBER: {
                switch (intentType) {
                    case Constants.CALL: {
                        IntentUtils.callNumber(ContextList.me, this, extractedVal);
                    }
                    break;
                    case Constants.SAVE_TO_CONTACTS: {
                        IntentUtils.savePhoneNumber(ContextList.me, extractedVal);
                    }
                    break;
                    case Constants.MESSAGE: {
                        IntentUtils.messageToNumber(ContextList.me, extractedVal);
                    }
                    break;
                    case Constants.SHARE: {
                        IntentUtils.shareInfo(ContextList.me, extractedVal, Constants.PHONENUMBER);
                    }
                    break;
                }
            }
            break;
            case Constants.ADDRESS: {
                switch (intentType) {
                    case Constants.SAVE_TO_CONTACTS: {
                        IntentUtils.saveAddress(ContextList.me, extractedVal);
                    }
                    break;
                    case Constants.SHARE: {
                        IntentUtils.shareInfo(ContextList.me, extractedVal, Constants.ADDRESS);
                    }
                    break;
                }
            }
            break;
            case Constants.URL: {
                switch (intentType) {
                    case Constants.OPEN: {
                        IntentUtils.openWebLink(ContextList.me, extractedVal);
                    }
                    break;
                    case Constants.SAVE_TO_CONTACTS: {
                        IntentUtils.saveURL(ContextList.me, extractedVal);
                    }
                    break;
                    case Constants.SHARE: {
                        IntentUtils.shareInfo(ContextList.me, extractedVal, Constants.URL);
                    }
                    break;
                }
            }
            break;
            case Constants.COMPLETETEXT: {
                switch (intentType) {
                    case Constants.SAVE: {
                        IntentUtils.saveData(ContextList.me, extractedVal);
                    }
                    break;
                    case Constants.SHARE: {
                        IntentUtils.shareInfo(ContextList.me, extractedVal, Constants.COMPLETETEXT);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void processHandlerAfterPermission() {
        handleRequest();
    }
}
