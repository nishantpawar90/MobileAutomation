package com.enterprise.mobile.data;

import net.datafaker.Faker;

/**
 * Dynamic test data generator using DataFaker.
 * Generates realistic test data for mobile automation scenarios.
 */
public final class TestDataGenerator {

    private static final Faker faker = new Faker();

    private TestDataGenerator() {
    }

    public static Faker getFaker() {
        return faker;
    }

    // ===== User Data =====

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateUsername() {
        return faker.internet().username();
    }

    public static String generatePassword() {
        return faker.internet().password(8, 16, true, true, true);
    }

    public static String generateFirstName() {
        return faker.name().firstName();
    }

    public static String generateLastName() {
        return faker.name().lastName();
    }

    public static String generateFullName() {
        return faker.name().fullName();
    }

    public static String generatePhoneNumber() {
        return faker.phoneNumber().cellPhone();
    }

    // ===== Address Data =====

    public static String generateStreetAddress() {
        return faker.address().streetAddress();
    }

    public static String generateCity() {
        return faker.address().city();
    }

    public static String generateZipCode() {
        return faker.address().zipCode();
    }

    public static String generateCountry() {
        return faker.address().country();
    }

    // ===== Payment Data =====

    public static String generateCreditCardNumber() {
        return faker.finance().creditCard();
    }

    public static String generateCVV() {
        return String.valueOf(faker.number().numberBetween(100, 999));
    }

    // ===== General =====

    public static String generateText(int maxLength) {
        return faker.lorem().characters(1, maxLength);
    }

    public static String generateSentence() {
        return faker.lorem().sentence();
    }

    public static int generateNumber(int min, int max) {
        return faker.number().numberBetween(min, max);
    }

    public static String generateUUID() {
        return faker.internet().uuid();
    }

    public static String generateCompanyName() {
        return faker.company().name();
    }

    public static String generateDate(String format) {
        return faker.date().birthday().toString();
    }
}
