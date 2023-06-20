package objektwerks

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

object Serializer:
  given JsonValueCodec[Entity] = JsonCodecMaker.make[Entity]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Account] = JsonCodecMaker.make[Account]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Profile] = JsonCodecMaker.make[Profile]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Edible] = JsonCodecMaker.make[Edible]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Drinkable] = JsonCodecMaker.make[Drinkable]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Expendable] = JsonCodecMaker.make[Expendable]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Measurable] = JsonCodecMaker.make[Measurable]( CodecMakerConfig.withDiscriminatorFieldName(None) )