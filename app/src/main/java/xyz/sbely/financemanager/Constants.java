package xyz.sbely.financemanager;

import xyz.sbely.financemanager.model.Category;

public final class Constants {
    private Constants() {
    }

    public static final Category[] INCREASE_CATEGORIES = {
            new Category(R.string.salary, R.drawable.ic_account_balance_wallet_black_24dp),
            new Category(R.string.stipend, R.drawable.ic_school_black_24dp),
            new Category(R.string.internate, R.drawable.ic_laptop_black_24dp),
            new Category(R.string.present, R.drawable.ic_cake_black_24dp),
            new Category(R.string.other, R.drawable.ic_monetization_on_black_24dp)
    };

    public static final Category[] DECREASE_CATEGORIES = {
            new Category(R.string.food, R.drawable.ic_restaurant_menu),
            new Category(R.string.purchases, R.drawable.ic_local_grocery_store),
            new Category(R.string.auto, R.drawable.ic_directions_car),
            new Category(R.string.child, R.drawable.ic_child_friendly),
            new Category(R.string.pay, R.drawable.ic_payment),
            new Category(R.string.leisure, R.drawable.ic_local_bar),
            new Category(R.string.other, R.drawable.ic_monetization_on_black_24dp)
    };

    public static final Integer[] IMAGE_NEW_CATEGORY_ID = {R.drawable.ic_location_city, R.drawable.ic_fitness_center, R.drawable.ic_beach_access, R.drawable.ic_flight,
            R.drawable.ic_wb_sunny, R.drawable.ic_headset, R.drawable.ic_favorite_border, R.drawable.ic_security};

    public static final int[] colors = {R.color.graphics1, R.color.graphics2, R.color.graphics3, R.color.graphics4, R.color.graphics5,
            R.color.graphics6, R.color.graphics7, R.color.graphics8, R.color.graphics9, R.color.graphics10, R.color.graphics11, R.color.graphics12,
            R.color.graphics13, R.color.graphics14, R.color.graphics15, R.color.graphics16, R.color.graphics17, R.color.graphics18, R.color.graphics19,
            R.color.graphics20, R.color.graphics21, R.color.graphics22};

    public static final int INCOME_SUCCESSFULLY_LOADED = 1;
    public static final int INCOME_UNSUCCESSFULLY_LOADED = 2;
    public static final int EXPENSE_SUCCESSFULLY_LOADED = 3;
    public static final int EXPENSE_UNSUCCESSFULLY_LOADED = 4;
    public static final int REPORT_SUCCESSFULLY_LOADED = 5;
    public static final int REPORT_UNSUCCESSFULLY_LOADED = 6;
    public static final int GRAPHICS_PIE_INC_SUCCESSFULLY_LOADED = 7;
    public static final int GRAPHICS_PIE_INC_UNSUCCESSFULLY_LOADED = 8;
    public static final int GRAPHICS_PIE_EXP_SUCCESSFULLY_LOADED = 9;
    public static final int GRAPHICS_PIE_EXP_UNSUCCESSFULLY_LOADED = 10;
    public static final int GRAPHICS_LINE_EXP_SUCCESSFULLY_LOADED = 11;
    public static final int GRAPHICS_LINE_EXP_UNSUCCESSFULLY_LOADED = 12;
    public static final int GRAPHICS_LINE_INC_SUCCESSFULLY_LOADED = 13;
    public static final int GRAPHICS_LINE_INC_UNSUCCESSFULLY_LOADED = 14;
}
