package com.prance.lib_sharedpreferences;

public enum DataType
{
    STRING(0), BOOLEAN(1), INT(2), DATA(3), LONG(4), FLOAT(5),;

    private int value;

    private DataType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    /**
     * 根据值获取当前数据类型对象
     *
     * @param value
     * @return
     */
    public static DataType getDataTypeByValue(int value)
    {
        for (DataType type : DataType.values())
        {
            if (type.value == value)
            {
                return type;
            }
        }
        return null;
    }
}
