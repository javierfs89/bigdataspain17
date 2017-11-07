# Ejercicio 2: Eq

Vamos a poner en práctica todo lo que hemos aprendido en esta parte de typeclasses, para ello, vamos a utilizar otra typeclass muy famosa y común, se llama `Eq` y simplemente dota de la habilidad para comparar si dos valores son iguales. La gente familiarizada con Java le recordará al método `equals` que todos los objetos tienen mediante herencia. Utilizando typeclasses desacoplamos este comportamiento, como ya hemos visto en los pasos anteriores.

1. Aquí está la typeclass

```tut
trait Eq[A] {
  def ===(a1: A, a2: A): Boolean
  def =!=(a1: A, a2: A): Boolean = !(===(a1, a2))
}
```

2. Define las siguientes funciones que hacen uso de la typeclass `Eq`:

```tut
def contains[A](l: List[A])(a: A)(E: Eq[A]): Boolean =
  l.foldLeft(false)((acc, a2) => acc || (E.===(a, a2)))

// More eficient
def contains[A](l: List[A])(a: A)(E: Eq[A]): Boolean =
  l match {
    case h :: t =>
      if (E.===(h, a)) true
      else contains(t)(a)(E)
    case Nil => false
  }

def index[A](l: List[A])(a: A)(E: Eq[A]): Option[Int] = {
  def go(ll: List[A], res: Int): Option[Int] =
    ll match {
      case h :: t =>
        if (E.===(h, a)) Some(res)
        else go(t, res+1)
      case Nil => None
    }
  go(l, 0)
}
```

3. Da instancias para poder ejecutar las funciones de arriba con los siguientes argumentos

```tut
val intEq: Eq[Int] =
  new Eq[Int] {
    def ===(i1: Int, i2: Int): Boolean = i1 == i2
  }

val stringEq: Eq[String] =
  new Eq[String] {
    def ===(s1: String, s2: String): Boolean = s1 == s2
  }
```

4. Ya podemos ejecutar las funciones con distintos parámetros de tipo `Int` y `String`.

```tut
contains(List(1, 2, 3))(4)(intEq)
contains(List(1, 2, 3))(2)(intEq)
index(List("hola", "a", "todo", "el", "mundo"))("el")(stringEq)
index(List("hola", "a", "todo", "el", "mundo"))("no_existe")(stringEq)
```

5. También podemos dar instancias para nuestros tipos propios.

```tut
case class Person(name: String, age: Int)
def personEq(ES: Eq[String], EI: Eq[Int]): Eq[Person] =
  new Eq[Person] {
    def ===(p1: Person, p2: Person): Boolean =
      ES.===(p1.name, p2.name) &&
      EI.===(p1.age, p2.age)
  }

contains(List(
  Person("Ana", 28),
  Person("Berto", 38),
  Person("Carlos", 18)))(Person("Carlos", 18))(personEq(stringEq, intEq))
```
