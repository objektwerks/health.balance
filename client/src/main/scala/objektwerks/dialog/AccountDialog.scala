package objektwerks.dialog

import scalafx.Includes.*
import scalafx.scene.control.{ButtonType, Dialog, Label}
import scalafx.scene.control.ButtonBar.ButtonData

import objektwerks.{Account, Client, Context, Deactivate, Entity, Reactivate}
import objektwerks.Validator.*
import objektwerks.layout.ControlGridPane

final case class DeactivateReactivate(deactivate: Option[Deactivate] = None,
                                      reactivate: Option[Reactivate] = None)

final class AccountDialog(context: Context, account: Account) extends Dialog[DeactivateReactivate]:
  initOwner(Client.stage)
  title = context.windowTitle
  headerText = context.dialogAccount

  val controls = List[(String, Label)](
    context.labelLicense -> Label( account.license ),
    context.labelEmailAddress -> Label( account.emailAddress ),
    context.labelPin -> Label( account.pin ),
    context.labelActivated -> Label( Entity.epochSecondToLocalDate(account.activated).toString ),
    context.labelDeactivated -> Label( Entity.epochSecondToLocalDate(account.deactivated).toString )
  )

  dialogPane().content = ControlGridPane(controls)

  val deactivateButtonType = new ButtonType(context.buttonDeactivate, ButtonData.OKDone)
  val reactivateButtonType = new ButtonType(context.buttonActivate, ButtonData.OKDone)
  dialogPane().buttonTypes = List(ButtonType.Close, deactivateButtonType, reactivateButtonType)

  val deactivateButton = dialogPane().lookupButton(deactivateButtonType)
  deactivateButton.disable = account.isActivated

  val reactivateButton = dialogPane().lookupButton(reactivateButtonType)
  reactivateButton.disable = account.isDeactivated

  resultConverter = dialogButton => {
    if dialogButton == deactivateButtonType then
      DeactivateReactivate(deactivate = Some( Deactivate( account.license ) ) )
    else if dialogButton == reactivateButtonType then
      DeactivateReactivate(reactivate = Some( Reactivate( account.license) ) )
    else null
  }