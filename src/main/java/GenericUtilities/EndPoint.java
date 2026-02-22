package GenericUtilities;

/**
 * Central endpoint constants used by current test suites.
 */
public interface EndPoint {

    String CASEFILEUPLOAD = "/caseservice/";
    String CASEFILEPENDING = "/caseservice/pending";
    String CASEFILEPENDINGV1 = "/caseservice/v1/pending";
    String CASEFILEPENDINGHARDCOPY = "/caseservice/pending/hardcopy/{case_code}";
    String CASEFILEIGNORE = "/caseservice/pending/ignore";
    String CASEFILEPENDINGSUMMARY = "/caseservice/pending/summary";
    String CASEFILEBULKUPLOAD = "/caseservice/bulk-upload";
    String CASEFILEGETUNVERIFIED = "/caseservice/unverified";
    String CASEFILEBUCKETMOVE = "/caseservice/file/{UnverifiedCode_1}";
    String CASEFILEGETVERIFIED = "/caseservice/v1/verified-files";
    String CASEFILEGETDELETED = "/caseservice/deleted";
    String CASEFILECLONING = "/caseservice/services/case/clone";
    String CASEFILEREALLOCATE = "/caseservice/services/case/reallocate";
    String CASEFILECONFIG = "/caseservice/config";
    String CASEFILECONFIGV1 = "/caseservice/v1/config";
    String CASEFILELOOKUP = "/caseservice/lookup";
    String CASEFILEUPLOADRAW = "/caseservice/raw";
    String CASEFILEMAP = "/caseservice/map";
    String CASEFILEREQUIRED = "/caseservice/required";
    String CASEFILEREQUIREDBULK = "/caseservice/required/bulk";

    String SEND_OTP = "/customer/customer/common/sms/send";
    String CONFIRM_OTP = "/customer/customer/common/sms/v1/verify";
}


