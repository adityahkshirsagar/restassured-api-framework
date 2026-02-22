package GenericUtilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

import com.github.javafaker.Faker;

public class JavaUtility {

	static Faker faker = new Faker();

	public  String randomNo()
	{
		int startDigit = faker.number().numberBetween(6, 9);
		String phone = startDigit+faker.numerify("#########");
		return phone;

	}

	public String randomlast5digit(String originalString)
	{
		 String modifiedString = originalString.substring(0, originalString.length() - 5)
	             + faker.number().digits(5);
		 return modifiedString;
	}

	public static String randommandate()
	{
		String mandate=RandomStringUtils.randomAlphanumeric(27);
		return ("ENA"+mandate+"AP");
	}

	public static String randomUMRN()
	{
		String umrn = RandomStringUtils.randomNumeric(15);
		return ("UMRN0"+umrn);
	}

	public  String generateMobileNumber() {
        Random random = new Random();
        StringBuilder mobileNumber = new StringBuilder("6");
        for (int i = 0; i < 9; i++) {
            int digit = random.nextInt(10);
            mobileNumber.append(digit);
        }

        return mobileNumber.toString();
    }

	public  String randomApplicationNo()
	{

		String lastPart = faker.regexify("[0-9]{5}");
		String firstFive = faker.regexify("[A-Z]{5}"); // Generate first five letters
		return "PEN-"+firstFive+"-000-SEP24-"+lastPart;
	}

	public static  String randomCode()
	{

		String code=RandomStringUtils.randomAlphanumeric(13).toLowerCase();
		return code;
	}

	public  String randomCode1()
	{

		String code=RandomStringUtils.randomAlphanumeric(1).toLowerCase();
		return code;
	}

	public String randomePan()
	{

		return faker.regexify("[A-Z]{3}"+"P"+"[A-Z]{1}"+"[0-9]{4}[A-Z]{1}");

	}

	public  String randomMonthlySalegreaterthan2()
	{
		long x= faker.number().numberBetween(100000, 199999);
		String amountString=Long.toString(x);
		return amountString;

	}

	public static String randomMonthlySaleLesserthan2()
	{
		long x= faker.number().numberBetween(200000, 999999);
		String amountString=Long.toString(x);
		return amountString;
	}

	public static int convertToInt(String stringData) {
		try {
			return Integer.parseInt(stringData);
		} catch (Exception e) {
			return 0;
		}

	}

	public static long convertToLong(String stringData) {
		try {
			return Long.parseLong(stringData);
		} catch (Exception e) {
			return 0;
		}

	}

	public static double convertToDouble(String stringData){
		try{
			return Double.parseDouble(stringData);
		}
		catch (Exception e){
			return 0;
		}
	}

	public  String randomEmailId()
	{
		String email=faker.internet().emailAddress();
		return email;

	}

	public static String partner_ref_no()
	{

		String generatedEmail = RandomStringUtils.randomNumeric(5);
		String Email="sample@example.invalid"+generatedEmail;
		return Email;
	}

	public static String partner_application_id()
	{

		String generatedEmail = RandomStringUtils.randomNumeric(5);
		String Email="sample@example.invalid"+generatedEmail;
		return Email;
	}

	/**
	 * This method will return current system date
	 * @return
	 */

	public String getSystemDate()
	{
		Date d = new Date(0);
		String date=d.toString();
		return date;
	}

	/**
	 * This method will provide the date in a specific format
	 * @return
	 */

	public String getSystemDateInFormat()
	{
		Date d = new Date(0);
		String [] dArr =d.toString().split(" ");
		String date=dArr[2];
		String month= dArr[1];
		String year = dArr[5];
		String time= dArr[3].replace(":", "-");
		String currentDate= date+" "+month+" "+year+" "+time;
		return currentDate;
	}

	public static String getCurrentDateFormatted() {
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
		return currentDate.format(formatter);

	}

	public static String getCurrentDateFormatted1() {
		LocalDate currentDate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
		return currentDate.format(formatter);
	}

	public static String getDayAfterTomorrowFormatted()
	{
		LocalDate currentDate = LocalDate.now();
		LocalDate dayAfterTomorrow = currentDate.plusDays(3);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
		return dayAfterTomorrow.format(formatter);

	}

	public static String getDayAfterTomorrowFormatted1()
	{
		LocalDate currentDate = LocalDate.now();
		LocalDate dayAfterTomorrow = currentDate.plusDays(3);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
		return dayAfterTomorrow.format(formatter);

	}

	public static String get1stOfNextNextMonth() {
		LocalDate currentDate = LocalDate.now();
		LocalDate nextNextMonth = currentDate.plusMonths(2);
		LocalDate date = LocalDate.of(nextNextMonth.getYear(), nextNextMonth.getMonth(), 1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return date.format(formatter);

	}

	public static String get1stOfNextMonth() {
		LocalDate currentDate = LocalDate.now();
		LocalDate nextMonth = currentDate.plusMonths(1);
		LocalDate date = LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), 1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return date.format(formatter);
	}

	/**
	 * This method is used to convert string to key & value & return in the for of map
	 *
	 * @param key_value
	 * @return
	 */

	public static String randomFirstName() {
        String firstName = faker.name().firstName();
        return firstName; // Combine first and last names
    }
	public static String randomMiddleName() {
        String lastName = faker.name().firstName();
        return lastName; // Combine first and last names
    }

	public static String randomLastName() {
        String lastName = faker.name().lastName();
        return lastName; // Combine first and last names
    }

	public static String randome4Num()
	{
		String generatedString2 = RandomStringUtils.randomNumeric(4);
		return (generatedString2);
	}

	public static String offerMobileNo()
	{
		String ninenumbers=RandomStringUtils.randomNumeric(9);
		return ("7"+ninenumbers);
	}
	public static String randome10Num()
	{
		String generatedString2 = RandomStringUtils.randomNumeric(10);
		return (generatedString2);
	}

	public static String random4numeric()
	{
		String get=RandomStringUtils.randomNumeric(7);
		return (get);
	}

	public static String get16thOfMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate date = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 16);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }

    public static String get16thOfNextMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate nextMonth = currentDate.plusMonths(1);
        LocalDate date = LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), 16);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }

    public static String get4thOfNextMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate nextMonth = currentDate.plusMonths(1);
        LocalDate date = LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), 4);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }

    public static String get4thOfNextNextMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate nextNextMonth = currentDate.plusMonths(2);
        LocalDate date = LocalDate.of(nextNextMonth.getYear(), nextNextMonth.getMonth(), 4);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);

    }

    public  String randomName()
	{

		 String firstName = faker.name().firstName();
	        String lastName = faker.name().lastName();
	        String fullNameWithoutSpace = firstName +" "+ lastName;

		return fullNameWithoutSpace;
	}
    public int  getRandomNumber()
	{
		Random r=new Random();
		int ran = r.nextInt(1000);
		return ran;
	}
    public static String randomckycNo()
    {
		String number = faker.regexify("[0-9]{9}");
		return "50031"+number;
    }
    public static String randompmandate()
	{
		String mandate=RandomStringUtils.randomAlphanumeric(27);
		return ("ENA"+mandate+"PH");
	}
}
