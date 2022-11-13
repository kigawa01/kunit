package net.kigawa.kutil.unit.exception;

public class UnitException extends Exception
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
