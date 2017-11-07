# Problema 1: Order

Queremos implementar la función greater, que coge una lista de elementos y devuelve el valor más grande de entre todos ellos.

```scala
def greater[A](l: List[A]): Option[A]
```

0. Esta función, como se puede comprobar, es inimplementable tal y como está. En el mundo OOP tradicional podemos utilizar una interfaz para dotar a nuestro tipo genérico `A` de la información necesaria para implementarla. Vamos a ver...

1. Empezamos con una interfaz OOP de toda la vida que nos permite comparar/ordenar elementos. En Scala las interfaces pueden estar parcialmente (o totalmente) implementadas.

```tut
trait Order[A] {
  def compare(other: A): Int

  def >(other: A): Boolean = compare(other) > 0
  def ===(other: A): Boolean = compare(other) == 0
  def <(other: A): Boolean = compare(other) < 0
}
```

2. Vamos a tratar de implementar ahora la función `greater` exigiendo que los elementos de la lista tienen que ser subtipos de la interfaz `Order` que acabamos de definir

```tut
def greatest[A <: Order[A]](l: List[A]): Option[A] =
  l.foldLeft(Option.empty[A]) {
    case (Some(max), a) if a < max => Option(max)
    case (_, a) => Option(a)
  }
```

3. Vamos a comprobar que esta función es correcta, para ello vamos a crearnos un tipo `Person` que implemente la interfaz `Order`.

```tut
case class Person(name: String, age: Int) extends Order[Person] {
  def compare(other: Person) = age - other.age
}
```

4. Ya tenemos todas las piezas del puzzle, solo hace falta ejecutar la función con una lista de personas y ver si el resultado es el esperado.

```tut
greatest(List(Person("Ana", 28), Person("Berto", 35), Person("Carlos", 18)))
```

5. Bueno, parece que sí, que todo funciona correctamente, pero... ¿qué sucede si no podemos hacer que el tipo herede de nuestra interfaz? Esto ocurre cuando no tenemos control sobre la definición del tipo en cuestión, ya sea porque sea un tipo primitivo del lenguaje, o que pertenezca a una librería externa.

```tut:fail
greatest(List(2, 3, 1))
```

6. En este caso, la OOP nos proporciona otra técnica para resolver este problema, y no es otra que los adaptadores. Para que nuestra función encaje con todos esos tipos fuera de nuestro control podemos crear un adaptador, y redefinir dicha función en terminos del adaptador

```tut
def greatest[A](l: List[A])(wrap: A => Order[A]): Option[A] =
  l.foldLeft(Option.empty[A]) {
    case (Some(max), a) if wrap(a) < max => Option(max)
    case (_, a) => Option(a)
  }
```

7. Parece que lo hemos conseguido, ahora deberíamos ser capaces de ordenar listas de enteros, cuando antes no podiamos

```tut
case class IntOrder(unwrap: Int) extends Order[Int] {
  def compare(other: Int) = unwrap - other
}
greatest(List(2, 3, 1))(IntOrder(_))
```

8. Esta solución tiene un problema de eficiencia tal y como está, ya que por cada elemento tenemos que "inyectarlo" en el adaptador, pero no nos vamos a centrar en ese problema por ahora. Damos el problema por solucionado, nuestra función `greatest` está definida de una manera genérica y podemos utilizarla con cualquier tipo siempre que demos una implementación para la función `compare`.
