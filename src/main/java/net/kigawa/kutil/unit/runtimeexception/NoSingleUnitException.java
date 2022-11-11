package net.kigawa.kutil.unit.runtimeexception;

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
