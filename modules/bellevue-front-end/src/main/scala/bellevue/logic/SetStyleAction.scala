package bellevue.logic

import bellevue.domain.*
import bellevue.domain.tools.Tool
import bellevue.logic.context.{Behavior, Variation}
import cats.effect.IO
import tyrian.Cmd

trait SetStyleAction extends Variation.Monoidal[DrawingModel, Cmd[IO, Msg]]:
  self: DrawingEnvironment =>

  override val run: Behavior[DrawingModel, Cmd[IO, Msg]] = partialExecAndMerge: model =>
    model.selectedTool match
      case Tool.Brush | Tool.Circle | Tool.Rectangle => canvas.run(_.setStyle(model.brushConfig))
      case Tool.Eraser                               => canvas.run(_.setStyle(model.eraserConfig.style))
