# Ejercicio 1: Show

Vamos a poner en práctica todo lo que hemos aprendido en esta parte de typeclasses, para ello, vamos a utilizar otra typeclass muy famosa y común, se llama `Show` y simplemente dota de la habilidad para representar un valor en formato `String`. La gente familiarizada con Java le recordará al método `toString` que todos los objetos tienen mediante herencia. Utilizando typeclasses desacoplamos este comportamiento, como ya hemos visto en los pasos anteriores.

1. Aquí está la typeclass

```tut
trait Show[A] {
  def show(a: A): String
}
```

2. Define las siguientes funciones que hacen uso de la typeclass `Show`:

```tut
def thirdString[A](l: List[A])(S: Show[A]): Option[String] =
  l.drop(2).headOption.map(S.show)

def sum(i1: Int, i2: Int)(S: Show[Int]): String =
  S.show(i1 + i2)
```

3. Da instancias para poder ejecutar las funciones de arriba con los siguientes argumentos

```tut
val intShow: Show[Int] =
  new Show[Int] {
    def show(a: Int): String = a.toString
  }
val stringShow: Show[String] =
  new Show[String] {
    def show(a: String): String = a
  }
```

4. Ya podemos ejecutar las funciones con distintos parámetros de tipo `Int` y `String`.

```tut
thirdString(List(1, 2, 3))(intShow)
thirdString(List("hello", "world!"))(stringShow)
thirdString(List("hello", ",", "world!"))(stringShow)
sum(5, 8)(intShow)
```

5. Tambien podemos dar instancias para nuestros tipos propios.

```tut
case class Person(name: String, age: Int)
def personShow(SS: Show[String], IS: Show[Int]): Show[Person] =
  new Show[Person] {
    def show(a: Person): String = s"${SS.show(a.name)} - ${IS.show(a.age)}"
  }

thirdString(List(Person("Ana", 28), Person("Berto", 38), Person("Carlos", 18)))(personShow(stringShow, intShow))
```

| [<< Prev: 2-Monoid.md](2-Monoid.md) | [Next: 4-Exercise2-Eq.md >>](4-Eq.md) |
| :--- | ---: |
