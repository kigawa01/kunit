package net.kigawa.kutil.unit;

public class RuntimeUnitException extends RuntimeException
{
    public RuntimeUnitException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public RuntimeUnitException(String message)
    {
        super(message);
    }
}
