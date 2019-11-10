package com.example.ecom.utils;

public class CONSTANTS {

    public static class NetworkRequestUrls {
        public static final String FETCH_PRODUCT_LIST = "http://www.mocky.io/v2/5dc749823800002035cded0d";
        public static final String FETCH_CART_PRODUCTS = "http://www.mocky.io/v2/5dc841a23000005f00e1dfb4";
        public static final String FETCH_DELIVERY_DETAIL_LIST = "http://www.mocky.io/v2/5dc807093000005e00e1df33";

        public static final String ADD_TO_CART = "http://www.mocky.io/v2/5dc1524e330000b3a41a5129?mocky-delay=1000ms";

        public static final String UPDATE_CART = "http://www.mocky.io/v2/5dc1524e330000b3a41a5129";
        public static final String UPDATE_FAVORITE_STATUS = "http://www.mocky.io/v2/5dc1524e330000b3a41a5129";

        public static final String REMOVE_FROM_CART = "http://www.mocky.io/v2/5dc1524e330000b3a41a5129";

        public static final String ADD_DELIVERY_DETAIL = "http://www.mocky.io/v2/5dc1524e330000b3a41a5129";
        public static final String UPDATE_DELIVERY_DETAIL = "http://www.mocky.io/v2/5dc1524e330000b3a41a5129";
        public static final String DELETE_DELIVERY_DETAIL = "http://www.mocky.io/v2/5dc1524e330000b3a41a5129";


    }

    public static class NetworkRequestTags {
        public static final String FETCH_PRODUCT_LIST = "0";
        public static final String FETCH_CART_PRODUCTS = "1";
        public static final String FETCH_DELIVERY_DETAIL_LIST = "2";

        public static final String ADD_TO_CART = "3";

        public static final String UPDATE_CART = "4";
        public static final String UPDATE_FAVORITE_STATUS = "5";

        public static final String REMOVE_FROM_CART = "6";

        public static final String ADD_DELIVERY_DETAIL = "7";
        public static final String UPDATE_DELIVERY_DETAIL = "8";
        public static final String DELETE_DELIVERY_DETAIL = "9";
    }
}
