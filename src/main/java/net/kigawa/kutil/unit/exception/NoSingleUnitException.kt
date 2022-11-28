package net.kigawa.kutil.unit.exception;

public class NoSingleUnitException extends RuntimeUnitException
{
    public NoSingleUnitException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public NoSingleUnitException(String message)
    {
        super(message);
    }
}
