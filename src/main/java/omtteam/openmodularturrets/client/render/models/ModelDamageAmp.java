// Date: 2014/09/28 01:18:28 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package omtteam.openmodularturrets.client.render.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
public class ModelDamageAmp extends ModelBase {
    private final ModelRenderer bottomBar;
    private final ModelRenderer rod1;
    private final ModelRenderer rod2;
    private final ModelRenderer rod3;
    private final ModelRenderer rod4;
    private final ModelRenderer rod5;
    private final ModelRenderer rod6;

    public ModelDamageAmp() {
        textureWidth = 64;
        textureHeight = 64;

        bottomBar = new ModelRenderer(this, 4, 15);
        bottomBar.addBox(-1.5F, 1F, -13F, 3, 1, 11);
        bottomBar.setRotationPoint(0F, 16F, 0F);
        bottomBar.setTextureSize(64, 64);
        bottomBar.mirror = true;
        setRotation(bottomBar, 0F, 0F, 0F);

        rod1 = new ModelRenderer(this, 0, 0);
        rod1.addBox(1.001F, -5F, -13F, 1, 6, 1);
        rod1.setRotationPoint(0F, 16F, 0F);
        rod1.setTextureSize(64, 64);
        rod1.mirror = true;
        setRotation(rod1, 0F, 0F, 0F);

        rod2 = new ModelRenderer(this, 0, 0);
        rod2.addBox(1.001F, -5F, -11F, 1, 6, 1);
        rod2.setRotationPoint(0F, 16F, 0F);
        rod2.setTextureSize(64, 64);
        rod2.mirror = true;
        setRotation(rod2, 0F, 0F, 0F);

        rod3 = new ModelRenderer(this, 0, 0);
        rod3.addBox(-2.001F, -5F, -9F, 1, 6, 1);
        rod3.setRotationPoint(0F, 16F, 0F);
        rod3.setTextureSize(64, 64);
        rod3.mirror = true;
        setRotation(rod3, 0F, 0F, 0F);

        rod4 = new ModelRenderer(this, 0, 0);
        rod4.addBox(-2.001F, -5F, -13F, 1, 6, 1);
        rod4.setRotationPoint(0F, 16F, 0F);
        rod4.setTextureSize(64, 64);
        rod4.mirror = true;
        setRotation(rod4, 0F, 0F, 0F);

        rod5 = new ModelRenderer(this, 0, 0);
        rod5.addBox(-2.001F, -5F, -11F, 1, 6, 1);
        rod5.setRotationPoint(0F, 16F, 0F);
        rod5.setTextureSize(64, 64);
        rod5.mirror = true;
        setRotation(rod5, 0F, 0F, 0F);

        rod6 = new ModelRenderer(this, 0, 0);
        rod6.addBox(1.001F, -5F, -9F, 1, 6, 1);
        rod6.setRotationPoint(0F, 16F, 0F);
        rod6.setTextureSize(64, 64);
        rod6.mirror = true;
        setRotation(rod6, 0F, 0F, 0F);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        bottomBar.render(scale);
        rod1.render(scale);
        rod2.render(scale);
        rod3.render(scale);
        rod4.render(scale);
        rod5.render(scale);
        rod6.render(scale);
    }

    @SuppressWarnings("SameParameterValue")
    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public void setRotationForTarget(float y, float z) {
        bottomBar.rotateAngleX = y;
        bottomBar.rotateAngleY = z;
        rod1.rotateAngleX = y;
        rod1.rotateAngleY = z;
        rod2.rotateAngleX = y;
        rod2.rotateAngleY = z;
        rod3.rotateAngleX = y;
        rod3.rotateAngleY = z;
        rod4.rotateAngleX = y;
        rod4.rotateAngleY = z;
        rod5.rotateAngleX = y;
        rod5.rotateAngleY = z;
        rod6.rotateAngleX = y;
        rod6.rotateAngleY = z;
    }

    public void renderAll() {
        bottomBar.render(0.0625F);
        rod1.render(0.0625F);
        rod2.render(0.0625F);
        rod3.render(0.0625F);
        rod4.render(0.0625F);
        rod5.render(0.0625F);
        rod6.render(0.0625F);
    }
}
