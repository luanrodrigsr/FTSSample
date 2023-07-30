package model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.FullText
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Product : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()

    @FullText // Previous: No annotation
    var description: String = ""

    @FullText // Previous: No annotation
    var brand: String = ""

    var sku: String = ""
    var archived: Boolean = false
}