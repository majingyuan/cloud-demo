package com.jd.nebulae.common.entity;

public enum DataType {

    UNKNOW("00000"), TAGS("00001"), LEADER("00002"),


    HIVE("01000"), HIVE_TABLE("01001"), HIVE_COLUMN("01002"), HIVE_TABLE_EXTEND("01003"), HIVE_COLUMN_EXTEND("01004"),
    HIVE_PARTITIONS("01005"), HIVE_PARTITION_COLUMN("01007"), HIVE_VIEW("01006"), HIVE_COLUMN_ENUMERATION("01008"), HIVE_PARTITION_COLUMN_EXTEND("01009"), EXPIRED_HIVE_TABLE("01010"),


    MYSQL("02000"), MYSQL_TABLE("02001"), MYSQL_COLUMN("02002"), MYSQL_TABLE_EXTEND("02003"), MYSQL_COLUMN_EXTEND("02004"),


    EXTERNAL("03000"), EXTERNAL_DB("03001"), EXTERNAL_TABLE("03002"), EXTERNAL_DOMAIN("03003"), EXTERNAL_DB_EXTEND("03004"), EXTERNAL_TABLE_EXTEND("03005");

    private String value;

    DataType(String value) {
        this.value = value;
    }

    public static DataType of(String value) {
        for (DataType dataType : DataType.values()) {
            if (dataType.value.equals(value)) {
                return dataType;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
