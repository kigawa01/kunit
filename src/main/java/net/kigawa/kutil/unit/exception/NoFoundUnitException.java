package net.kigawa.kutil.unit.exception;

public class NoFoundUnitException extends RuntimeUnitException
{
    public NoFoundUnitException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public NoFoundUnitException(String message)
    {
        super(message);
    }
}
