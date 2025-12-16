plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-common")
}

dependencies {
    modCompileOnlyApi(libs.puzzleslib.common)
}

multiloader {
    mixins {
        clientMixin("EquipmentLayerRendererMixin", "FoilTypeMixin", "ItemRendererMixin", "LevelRendererMixin", "ModelFeatureRendererMixin", "ModelWrapperMixin", "ThrownTridentRendererMixin")
    }
}
