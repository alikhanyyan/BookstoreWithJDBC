package org.example;

public enum MenuCodes {
    EXIT("0"),
    UPDATE_BOOK_DETAILS("1"),
    LIST_BOOKS_BY_GENRE("2"),
    LIST_BOOKS_BY_AUTHOR("3"),
    UPDATE_CUSTOMER_INFORMATION("4"),
    VIEW_CUSTOMER_PURCHASE_HISTORY("5"),
    PROCESS_NEW_SALE("6"),
    CALCULATE_TOTAL_REVENUE_BY_GENRE("7"),
    GENERATE_SALES_REPORT("8"),
    GENERATE_REVENUE_REPORT_BY_GENRE("9");

    private final String value;

    MenuCodes(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
