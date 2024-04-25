package objektwerks

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

object Serializer:
  given JsonValueCodec[Entity] = JsonCodecMaker.make[Entity]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Account] = JsonCodecMaker.make[Account]
  given JsonValueCodec[Profile] = JsonCodecMaker.make[Profile]
  given JsonValueCodec[Edible] = JsonCodecMaker.make[Edible]
  given JsonValueCodec[Drinkable] = JsonCodecMaker.make[Drinkable]
  given JsonValueCodec[Expendable] = JsonCodecMaker.make[Expendable]
  given JsonValueCodec[Measurable] = JsonCodecMaker.make[Measurable]

  given JsonValueCodec[Command] = JsonCodecMaker.make[Command]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[License] = JsonCodecMaker.make[License]
  given JsonValueCodec[Register] = JsonCodecMaker.make[Register]
  given JsonValueCodec[Login] = JsonCodecMaker.make[Login]
  given JsonValueCodec[Deactivate] = JsonCodecMaker.make[Deactivate]
  given JsonValueCodec[Reactivate] = JsonCodecMaker.make[Reactivate]
  given JsonValueCodec[ListProfiles] = JsonCodecMaker.make[ListProfiles]
  given JsonValueCodec[AddProfile] = JsonCodecMaker.make[AddProfile]
  given JsonValueCodec[UpdateProfile] = JsonCodecMaker.make[UpdateProfile]
  given JsonValueCodec[ListEdibles] = JsonCodecMaker.make[ListEdibles]
  given JsonValueCodec[AddEdible] = JsonCodecMaker.make[AddEdible]
  given JsonValueCodec[UpdateEdible] = JsonCodecMaker.make[UpdateEdible]
  given JsonValueCodec[ListDrinkables] = JsonCodecMaker.make[ListDrinkables]
  given JsonValueCodec[AddDrinkable] = JsonCodecMaker.make[AddDrinkable]
  given JsonValueCodec[UpdateDrinkable] = JsonCodecMaker.make[UpdateDrinkable]
  given JsonValueCodec[ListExpendables] = JsonCodecMaker.make[ListExpendables]
  given JsonValueCodec[AddExpendable] = JsonCodecMaker.make[AddExpendable]
  given JsonValueCodec[UpdateExpendable] = JsonCodecMaker.make[UpdateExpendable]
  given JsonValueCodec[ListMeasurables] = JsonCodecMaker.make[ListMeasurables]
  given JsonValueCodec[AddMeasurable] = JsonCodecMaker.make[AddMeasurable]
  given JsonValueCodec[UpdateMeasurable] = JsonCodecMaker.make[UpdateMeasurable]
  given JsonValueCodec[AddFault] = JsonCodecMaker.make[AddFault]

  given JsonValueCodec[Event] = JsonCodecMaker.make[Event]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Registered] = JsonCodecMaker.make[Registered]
  given JsonValueCodec[LoggedIn] = JsonCodecMaker.make[LoggedIn]
  given JsonValueCodec[Fault] = JsonCodecMaker.make[Fault]
  given JsonValueCodec[ProfilesListed] = JsonCodecMaker.make[ProfilesListed]
  given JsonValueCodec[ProfileAdded] = JsonCodecMaker.make[ProfileAdded]
  given JsonValueCodec[ProfileUpdated] = JsonCodecMaker.make[ProfileUpdated]
  given JsonValueCodec[EdiblesListed] = JsonCodecMaker.make[EdiblesListed]
  given JsonValueCodec[EdibleAdded] = JsonCodecMaker.make[EdibleAdded]
  given JsonValueCodec[EdibleUpdated] = JsonCodecMaker.make[EdibleUpdated]
  given JsonValueCodec[DrinkablesListed] = JsonCodecMaker.make[DrinkablesListed]
  given JsonValueCodec[DrinkableAdded] = JsonCodecMaker.make[DrinkableAdded]
  given JsonValueCodec[DrinkableUpdated] = JsonCodecMaker.make[DrinkableUpdated]
  given JsonValueCodec[ExpendablesListed] = JsonCodecMaker.make[ExpendablesListed]
  given JsonValueCodec[ExpendableAdded] = JsonCodecMaker.make[ExpendableAdded]
  given JsonValueCodec[ExpendableUpdated] = JsonCodecMaker.make[ExpendableUpdated]
  given JsonValueCodec[MeasurablesListed] = JsonCodecMaker.make[MeasurablesListed]
  given JsonValueCodec[MeasurableAdded] = JsonCodecMaker.make[MeasurableAdded]
  given JsonValueCodec[MeasurableUpdated] = JsonCodecMaker.make[MeasurableUpdated]
  given JsonValueCodec[FaultAdded] = JsonCodecMaker.make[FaultAdded]
