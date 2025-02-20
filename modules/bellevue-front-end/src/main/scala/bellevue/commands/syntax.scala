package bellevue.commands

import cats.effect.IO
import tyrian.Cmd

extension [A](action: IO[A])

  def command: Cmd[IO, A] =
    Cmd.Run(action)

  def run(f: A => IO[Unit]): Cmd[IO, Nothing] =
    Cmd.SideEffect(action.flatMap(f))
