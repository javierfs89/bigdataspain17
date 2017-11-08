# Ejercicio 1: Show

Vamos a poner en práctica todo lo que hemos aprendido en esta parte de typeclasses, para ello, vamos a utilizar otra typeclass muy famosa y común, se llama `Show` y simplemente dota de la habilidad para representar un valor en formato `String`. La gente familiarizada con Java le recordará al método `toString` que todos los objetos tienen mediante herencia. Utilizando typeclasses desacoplamos este comportamiento, como ya hemos visto en los pasos anteriores.

1. Aquí está la typeclass

```scala
scala> trait Show[A] {
     |   def show(a: A): String
     | }
defined trait Show
```

2. Define las siguientes funciones que hacen uso de la typeclass `Show`:

```scala
scala> def thirdString[A](l: List[A])(S: Show[A]): Option[String] =
     |   l.drop(2).headOption.map(S.show)
thirdString: [A](l: List[A])(S: Show[A])Option[String]

scala> def sum(i1: Int, i2: Int)(S: Show[Int]): String =
     |   S.show(i1 + i2)
sum: (i1: Int, i2: Int)(S: Show[Int])String
```

3. Da instancias para poder ejecutar las funciones de arriba con los siguientes argumentos

```scala
scala> val intShow: Show[Int] =
     |   new Show[Int] {
     |     def show(a: Int): String = a.toString
     |   }
intShow: Show[Int] = $anon$1@2e49e494

scala> val stringShow: Show[String] =
     |   new Show[String] {
     |     def show(a: String): String = a
     |   }
stringShow: Show[String] = $anon$1@457c7208
```

4. Ya podemos ejecutar las funciones con distintos parámetros de tipo `Int` y `String`.

```scala
scala> thirdString(List(1, 2, 3))(intShow)
res0: Option[String] = Some(3)

scala> thirdString(List("hello", "world!"))(stringShow)
res1: Option[String] = None

scala> thirdString(List("hello", ",", "world!"))(stringShow)
res2: Option[String] = Some(world!)

scala> sum(5, 8)(intShow)
res3: String = 13
```

5. Tambien podemos dar instancias para nuestros tipos propios.

```scala
scala> case class Person(name: String, age: Int)
defined class Person

scala> def personShow(SS: Show[String], IS: Show[Int]): Show[Person] =
     |   new Show[Person] {
     |     def show(a: Person): String = s"${SS.show(a.name)} - ${IS.show(a.age)}"
     |   }
personShow: (SS: Show[String], IS: Show[Int])Show[Person]

scala> thirdString(List(Person("Ana", 28), Person("Berto", 38), Person("Carlos", 18)))(personShow(stringShow, intShow))
res4: Option[String] = Some(Carlos - 18)
```
