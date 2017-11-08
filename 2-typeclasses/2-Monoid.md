# Problema 2: Monoid

Queremos implementar la función collapse, que coge una lista de elementos y devuelve el resultado de combinar asociativamente todos ellos. Este problema está muy relacionado con el Big Data y con la filosofía de Map/Reduce (sería la parte del _Reduce_)

```scala
def collapse[A](l: List[A]): A

scala> collapse(List(1, 2, 3, 4))
res0: Int = 10

scala> collapse(List("hello", ", ", "world!"))
res1: String = hello, world!
```

0. Al igual que el problema anterior, no es posible implementar está función tal y como está. Sin embargo, ya hemos visto que los adaptadores nos pueden servir para solucionar este tipo de problemas, así que vamos a intentarlo.

1. La interfaz que queremos tiene 2 operaciones:
1.1. Combine: nos permite combinar dos elementos en uno.
1.2. Empty: Nos ofrece un valor neutro para el caso en el que no haya elementos que combinar
1.3. Esta interfaz, en matemáticas se llama `Monoid`, así que utilizaremos este nombre, pero no te asustes, ya sabes que "sólo" sirve para reducir colecciones de elementos

```scala
trait Monoid[A] {
  def empty: A
  def combine(other: A): A
}

def collapse[A](l: List[A])(wrap: A => Monoid[A]): A =
  l.foldLeft[A](???)((a1, a2) => wrap(a1).combine(a2))
```

2. Vaya! Houston, tenemos un problema. No podemos dar un elemento neutro/inicial a nuestro `foldLeft`, por que no tenemos NINGÚN VALOR disponible. Para poder acceder al método `empty` necesitamos llamar a la función `wrap`, pero no tenemos ningún valor de tipo `A`. Esta función `wrap` tiene una limitación clara, solo puedes tener un `Monoid` si tienes un `A`, es decir, la interfaz está acoplada a valores. Como el elemento neutro conceptualmente no está ligado a ningún valor concreto, no tiene sentido el método `empty` que hemos puesto en la interfaz `Monoid`.

3. Vale, parece que lo que necesitamos es una interfaz, pero que esté desacoplada de los valores, y que su única relación sea con el tipo concreto con el que está parametrizado.

```scala
trait Monoid[A] {
  val empty: A
  def combine(a1: A, a2: A): A
}
```

4. ¡Acabamos de descubrir las Typeclasses! como ves es simplemente una interfaz, al igual que antes, pero no está pensada para ser heredada, si no para formar un módulo completamente independiente. Veamos que pasa si en vez del adaptador utilizamos la typeclass

```scala
def collapse[A](l: List[A])(monoid: Monoid[A]): A =
  l.foldLeft(monoid.empty)(monoid.combine)
```

5. Como vemos, en este caso no necesitamos un `A` para acceder a la interfaz `Monoid`, podemos acceder a ella directamente, ya que es un módulo concreto e independiente.

6. Como ventaja adicional, esta solución no tiene los problemas de eficiencia que teniamos con los adaptadores, ya que no se están creando objetos intermedios extra. La typeclass es simplement un conjunto de funciones/valores.

7. Al igual que con los adaptadores, tenemos que dar implementación a los métodos abstractos de la interfaz. Vamos a dar instancias para `Int` y `String`

```scala
val intSumMonoid: Monoid[Int] =
  new Monoid[Int] {
    val empty: Int = 0
    def combine(i1: Int, i2: Int): Int = i1 + i2
  }

val intMulMonoid: Monoid[Int] =
  new Monoid[Int] {
    val empty: Int = 1
    def combine(i1: Int, i2: Int): Int = i1 * i2
  }

val stringMonoid: Monoid[String] =
  new Monoid[String] {
    val empty: String = ""
    def combine(s1: String, s2: String): String = s1 + s2
  }
```

8. Como vemos, podemos tener distintas instancias para el mismo tipo, no hay ningún problema. Ya solo queda ejecutar las funciones genéricas con estas instancias que acabamos de implementar.

```scala
scala> collapse(List(1, 2, 3, 4))(intSumMonoid)
res3: Int = 10

scala> collapse(List(1, 2, 3, 4))(intMulMonoid)
res4: Int = 24

scala> collapse(List("hello", ", ", "world!"))(stringMonoid)
res5: String = hello, world!
```

9. Por cierto, no es casualidad que la typeclass `Monoid` encaje tan bien con la función `foldLeft`, están íntimamente relacionadas.

10. Aún hay muchas mejoras sintácticas que se podrían conseguir, pero están fuera del objetivo del curso, ellas son implícitos, context bounds, sintaxis, instancias derivadas.

| [<< Prev: 1-Order.md](1-Order.md) | [Next: 3-Exercise1-Show.md >>](3-Show.md) |
| :--- | ---: |
