# kutil-unit

## About

* add unit for java app
* manage unit by container

## Usage

```pom.xml
<dependency>
  <groupId>net.kigawa.kutil</groupId>
  <artifactId>unit</artifactId>
  <version>1.4</version>
</dependency>
```

Unitの登録

register unit

```java
// java
import net.kigawa.kutil.unit.Unit;

@Unit
class Unit
{

}
```

```kotlin
// kotlin
import net.kigawa.kutil.unit.Unit

@Unit
object Unit {

}
```

Unitをロードして初期化する

load units and init them

```java
import net.kigawa.kutil.unit.container.UnitContainer;

class Main
{
    public static void main(String[] args) {
        var container = new UnitContainer(getClass());
    }
}
```

## Requirement

* java

## Author

* kigawa
    * kigawa.8390@gmail.com

# Making

## Version

### Example: 9.1

* **9.1**
    * **9**: major
    * **1**: miner

## ToDo
