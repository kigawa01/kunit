package net.kigawa.kutil.unit.exception;

public class UnitNotInitException extends RuntimeUnitException
{
    public UnitNotInitException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public UnitNotInitException(String message)
    {
        super(message);
    }
}
