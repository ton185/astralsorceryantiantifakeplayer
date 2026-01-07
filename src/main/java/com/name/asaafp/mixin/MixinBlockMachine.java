package com.name.asaafp.mixin;

import hellfirepvp.astralsorcery.common.block.BlockCustomName;
import hellfirepvp.astralsorcery.common.block.BlockMachine;
import hellfirepvp.astralsorcery.common.block.BlockVariants;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockMachine.class)
public abstract class MixinBlockMachine extends BlockContainer implements BlockCustomName, BlockVariants {

    protected MixinBlockMachine(Material materialIn) {
        super(materialIn);
    }

    @Shadow
    public abstract boolean handleSpecificActivateEvent(PlayerInteractEvent.RightClickBlock event);

    @Inject(method="func_180639_a", at = @At("HEAD"), remap = false)
    public void onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, CallbackInfoReturnable<Boolean> cir) {
        if (player instanceof FakePlayer || (player instanceof EntityPlayerMP && MiscUtils.isPlayerFakeMP((EntityPlayerMP)player))) {
            PlayerInteractEvent.RightClickBlock event = new PlayerInteractEvent.RightClickBlock(player, hand, pos, facing, new Vec3d(hitX, hitY, hitZ));
            handleSpecificActivateEvent(event);
        }
    }

    @Redirect(remap = false, method = "handleSpecificActivateEvent", at = @At(value = "INVOKE", target = "Lhellfirepvp/astralsorcery/common/util/MiscUtils;isPlayerFakeMP(Lnet/minecraft/entity/player/EntityPlayerMP;)Z"))
    private boolean isPlayerFake(EntityPlayerMP specificPlayerClass) {
        return false;
    }

}
