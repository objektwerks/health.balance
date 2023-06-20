package objektwerks

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.github.plokhotnyuk.jsoniter_scala.macros.*

object Serializer:
  given JsonValueCodec[Entity] = JsonCodecMaker.make[Entity]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Account] = JsonCodecMaker.make[Account]( CodecMakerConfig.withDiscriminatorFieldName(None) )
  given JsonValueCodec[Profile] = JsonCodecMaker.make[Profile]( CodecMakerConfig.withDiscriminatorFieldName(None) )