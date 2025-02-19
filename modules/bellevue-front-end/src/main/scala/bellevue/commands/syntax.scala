package bellevue.commands

import cats.effect.IO
import tyrian.Cmd

extension [A](action: IO[A])

  def run(f: A => IO[Unit]): Cmd[IO, Nothing] =
    Cmd.SideEffect(action.flatMap(f))

  def command[B](f: A => IO[B]): Cmd[IO, B] =
    Cmd.Run(action.flatMap(f))
