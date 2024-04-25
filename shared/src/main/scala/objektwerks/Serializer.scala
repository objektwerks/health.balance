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
  given JsonValueCodec[License] = JsonCodecMaker.make[License]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Register] = JsonCodecMaker.make[Register]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Login] = JsonCodecMaker.make[Login]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Deactivate] = JsonCodecMaker.make[Deactivate]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Reactivate] = JsonCodecMaker.make[Reactivate]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[ListProfiles] = JsonCodecMaker.make[ListProfiles]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[AddProfile] = JsonCodecMaker.make[AddProfile]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[UpdateProfile] = JsonCodecMaker.make[UpdateProfile]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[ListEdibles] = JsonCodecMaker.make[ListEdibles]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[AddEdible] = JsonCodecMaker.make[AddEdible]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[UpdateEdible] = JsonCodecMaker.make[UpdateEdible]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[ListDrinkables] = JsonCodecMaker.make[ListDrinkables]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[AddDrinkable] = JsonCodecMaker.make[AddDrinkable]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[UpdateDrinkable] = JsonCodecMaker.make[UpdateDrinkable]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[ListExpendables] = JsonCodecMaker.make[ListExpendables]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[AddExpendable] = JsonCodecMaker.make[AddExpendable]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[UpdateExpendable] = JsonCodecMaker.make[UpdateExpendable]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[ListMeasurables] = JsonCodecMaker.make[ListMeasurables]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[AddMeasurable] = JsonCodecMaker.make[AddMeasurable]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[UpdateMeasurable] = JsonCodecMaker.make[UpdateMeasurable]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[AddFault] = JsonCodecMaker.make[AddFault]( CodecMakerConfig.withDiscriminatorFieldName(None) )

  given JsonValueCodec[Event] = JsonCodecMaker.make[Event]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Registered] = JsonCodecMaker.make[Registered]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[LoggedIn] = JsonCodecMaker.make[LoggedIn]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Fault] = JsonCodecMaker.make[Fault]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[ProfilesListed] = JsonCodecMaker.make[ProfilesListed]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[ProfileAdded] = JsonCodecMaker.make[ProfileAdded]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[ProfileUpdated] = JsonCodecMaker.make[ProfileUpdated]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[EdiblesListed] = JsonCodecMaker.make[EdiblesListed]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[EdibleAdded] = JsonCodecMaker.make[EdibleAdded]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[EdibleUpdated] = JsonCodecMaker.make[EdibleUpdated]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[DrinkablesListed] = JsonCodecMaker.make[DrinkablesListed]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[DrinkableAdded] = JsonCodecMaker.make[DrinkableAdded]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[DrinkableUpdated] = JsonCodecMaker.make[DrinkableUpdated]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[ExpendablesListed] = JsonCodecMaker.make[ExpendablesListed]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[ExpendableAdded] = JsonCodecMaker.make[ExpendableAdded]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[ExpendableUpdated] = JsonCodecMaker.make[ExpendableUpdated]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[MeasurablesListed] = JsonCodecMaker.make[MeasurablesListed]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[MeasurableAdded] = JsonCodecMaker.make[MeasurableAdded]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[MeasurableUpdated] = JsonCodecMaker.make[MeasurableUpdated]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[FaultAdded] = JsonCodecMaker.make[FaultAdded]( CodecMakerConfig.withDiscriminatorFieldName(None) )
