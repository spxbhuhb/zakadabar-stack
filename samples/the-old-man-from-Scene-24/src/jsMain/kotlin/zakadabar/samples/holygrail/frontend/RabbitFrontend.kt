/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.samples.holygrail.frontend

//
//object RabbitFrontend : DtoFrontend<RabbitDto>(
//    HolyGrail.uuid,
//    RabbitDto.Companion
//) {
//    override val dtoClass = RabbitDto::class
//
//    override fun allView() = ComplexElement() launchBuild {
//        RabbitDto.all().forEach { dto ->
//            + div {
//                + dto.toString()
//                current.addEventListener("click", {
//                    Navigation.changeLocation(dto, Navigation.READ)
//                })
//            }
//        }
//    }
//
//    fun colorsView() = ComplexElement() launchBuild {
//        RabbitColors().execute().forEach { + div { + it.toString() } }
//    }
//
//    override fun createView() = ComplexElement() launchBuild {
//        val numbers = fourRandomInt()
//        val v = numbers.map { max(0, min(it, 255)) }
//
//        val dto = RabbitDto(
//            id = 0,
//            name = "Rabbit # ${numbers[3]}",
//            color = "rgba(${v[0]},${v[1]},${v[2]},1)"
//        ).create()
//
//        + col {
//            + div { + "Created a new rabbit:" }
//            + div { + "# ${dto.id}" }
//            + div { + dto.name }
//            + div { + dto.color }
//        }
//    }
//
//    override fun readView() = ComplexElement() launchBuild {
//        val dto = RabbitDto.read(requireNotNull(Navigation.state.viewState?.localId))
//        + col {
//            + div { + "Rabbit # ${dto.id}" }
//            + div { + dto.name }
//            + div { + dto.color }
//        }
//    }
//
//}