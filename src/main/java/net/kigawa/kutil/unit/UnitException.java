package net.kigawa.kutil.unit;

public class UnitException extends RuntimeException
{
    public UnitException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public UnitException(String message)
    {
        super(message);
    }
}
