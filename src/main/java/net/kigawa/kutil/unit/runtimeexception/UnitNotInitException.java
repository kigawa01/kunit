package net.kigawa.kutil.unit.runtimeexception;

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
