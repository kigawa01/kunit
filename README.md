# kunit

## About

* add DI Container for java app
* inject dependency instance to unit constructor
* manage unit by container

## Usage

```pom.xml
<dependency>
  <groupId>net.kigawa.kutil</groupId>
  <artifactId>kunit-unit</artifactId>
  <version>4.4.0</version>
</dependency>
```

Unitの登録

register unit

```java
// java

import net.kigawa.kutil.unit.annotation.Unit;

@Kunit
class Unit
{

}
```

```kotlin
// kotlin
import net.kigawa.kutil.unit.annotation.Kunit

@Knnit
object Unit {

}
```

Unitをロードして初期化する

load units and init them

```java


class Main
{
  public static void main(String[] args) {
    // init
    var container = UnitContainer.create();
    container.getUnit(ResourceRegistrar.class).register(UnitContainerTest.class);

    // shutdown
    container.close();
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

### Example: 9.1.2

* **9**: major, destructive
* **1**: miner, new function
* **2**: miner, bug fix

## ToDo
