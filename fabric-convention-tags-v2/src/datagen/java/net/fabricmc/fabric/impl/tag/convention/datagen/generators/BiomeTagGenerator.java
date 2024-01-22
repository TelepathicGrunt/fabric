/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.impl.tag.convention.datagen.generators;

import java.util.concurrent.CompletableFuture;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil;

public final class BiomeTagGenerator extends FabricTagProvider<Biome> {
	public BiomeTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
		super(output, RegistryKeys.BIOME, completableFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup arg) {
		generateDimensionTags();
		generateCategoryTags();
		generateOtherBiomeTypes();
		generateClimateAndVegetationTags();
		generateTerrainDescriptorTags();
		generateBackwardsCompatTags();
	}

	private void generateDimensionTags() {
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_NETHER)
				.addOptionalTag(BiomeTags.IS_NETHER)
				.add(BiomeKeys.CRIMSON_FOREST)
				.add(BiomeKeys.WARPED_FOREST)
				.add(BiomeKeys.NETHER_WASTES)
				.add(BiomeKeys.SOUL_SAND_VALLEY)
				.add(BiomeKeys.BASALT_DELTAS);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_THE_END)
				.addOptionalTag(BiomeTags.IS_END)
				.add(BiomeKeys.END_BARRENS)
				.add(BiomeKeys.END_MIDLANDS)
				.add(BiomeKeys.END_HIGHLANDS)
				.add(BiomeKeys.THE_END)
				.add(BiomeKeys.SMALL_END_ISLANDS);
		// We avoid the vanilla group tags here as mods may add to them without actually spawning them in the overworld
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_OVERWORLD)
				.addOptionalTag(BiomeTags.IS_OVERWORLD)
				.add(BiomeKeys.RIVER, BiomeKeys.FROZEN_RIVER)
				.add(BiomeKeys.COLD_OCEAN, BiomeKeys.DEEP_COLD_OCEAN)
				.add(BiomeKeys.DEEP_FROZEN_OCEAN, BiomeKeys.DEEP_OCEAN)
				.add(BiomeKeys.DEEP_LUKEWARM_OCEAN, BiomeKeys.WARM_OCEAN, BiomeKeys.LUKEWARM_OCEAN)
				.add(BiomeKeys.FROZEN_OCEAN, BiomeKeys.OCEAN)
				.add(BiomeKeys.BEACH, BiomeKeys.PLAINS)
				.add(BiomeKeys.SUNFLOWER_PLAINS, BiomeKeys.SNOWY_PLAINS)
				.add(BiomeKeys.ICE_SPIKES, BiomeKeys.DESERT)
				.add(BiomeKeys.FOREST, BiomeKeys.FLOWER_FOREST)
				.add(BiomeKeys.BIRCH_FOREST, BiomeKeys.DARK_FOREST)
				.add(BiomeKeys.OLD_GROWTH_BIRCH_FOREST, BiomeKeys.OLD_GROWTH_PINE_TAIGA)
				.add(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA, BiomeKeys.TAIGA)
				.add(BiomeKeys.SNOWY_TAIGA, BiomeKeys.SAVANNA)
				.add(BiomeKeys.SAVANNA_PLATEAU, BiomeKeys.WINDSWEPT_HILLS)
				.add(BiomeKeys.WINDSWEPT_GRAVELLY_HILLS, BiomeKeys.WINDSWEPT_FOREST)
				.add(BiomeKeys.WINDSWEPT_SAVANNA, BiomeKeys.JUNGLE, BiomeKeys.SPARSE_JUNGLE)
				.add(BiomeKeys.BAMBOO_JUNGLE, BiomeKeys.BADLANDS, BiomeKeys.ERODED_BADLANDS)
				.add(BiomeKeys.WOODED_BADLANDS, BiomeKeys.MEADOW, BiomeKeys.GROVE)
				.add(BiomeKeys.SNOWY_SLOPES, BiomeKeys.FROZEN_PEAKS, BiomeKeys.JAGGED_PEAKS)
				.add(BiomeKeys.STONY_PEAKS, BiomeKeys.MUSHROOM_FIELDS, BiomeKeys.DRIPSTONE_CAVES)
				.add(BiomeKeys.LUSH_CAVES, BiomeKeys.SNOWY_BEACH, BiomeKeys.SWAMP, BiomeKeys.STONY_SHORE)
				.add(BiomeKeys.DEEP_DARK, BiomeKeys.MANGROVE_SWAMP)
				.add(BiomeKeys.CHERRY_GROVE);
	}

	private void generateCategoryTags() {
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_TAIGA)
				.addOptionalTag(BiomeTags.IS_TAIGA);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_EXTREME_HILLS)
				.add(BiomeKeys.WINDSWEPT_GRAVELLY_HILLS)
				.add(BiomeKeys.WINDSWEPT_HILLS);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_WINDSWEPT)
				.add(BiomeKeys.WINDSWEPT_HILLS)
				.add(BiomeKeys.WINDSWEPT_GRAVELLY_HILLS)
				.add(BiomeKeys.WINDSWEPT_FOREST)
				.add(BiomeKeys.WINDSWEPT_SAVANNA);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_JUNGLE)
				.addOptionalTag(BiomeTags.IS_JUNGLE);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_PLAINS)
				.add(BiomeKeys.SUNFLOWER_PLAINS)
				.add(BiomeKeys.PLAINS);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_SAVANNA)
				.addOptionalTag(BiomeTags.IS_SAVANNA)
				.add(BiomeKeys.SAVANNA_PLATEAU)
				.add(BiomeKeys.WINDSWEPT_SAVANNA)
				.add(BiomeKeys.SAVANNA);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_ICY)
				.add(BiomeKeys.FROZEN_PEAKS)
				.add(BiomeKeys.ICE_SPIKES);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_AQUATIC_ICY)
				.add(BiomeKeys.FROZEN_RIVER)
				.add(BiomeKeys.DEEP_FROZEN_OCEAN)
				.add(BiomeKeys.FROZEN_OCEAN);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_SNOWY)
				.add(BiomeKeys.SNOWY_BEACH)
				.add(BiomeKeys.SNOWY_PLAINS)
				.add(BiomeKeys.SNOWY_SLOPES)
				.add(BiomeKeys.SNOWY_TAIGA);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_BEACH)
				.addOptionalTag(BiomeTags.IS_BEACH)
				.add(BiomeKeys.STONY_SHORE);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_FOREST)
				.addOptionalTag(BiomeTags.IS_FOREST);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_BIRCH_FOREST)
				.add(BiomeKeys.BIRCH_FOREST)
				.add(BiomeKeys.OLD_GROWTH_BIRCH_FOREST);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_OCEAN)
				.addOptionalTag(ConventionalBiomeTags.IS_DEEP_OCEAN)
				.addOptionalTag(ConventionalBiomeTags.IS_SHALLOW_OCEAN)
				.addOptionalTag(BiomeTags.IS_OCEAN);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_DESERT)
				.add(BiomeKeys.DESERT);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_RIVER)
				.addOptionalTag(BiomeTags.IS_RIVER);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_SWAMP)
				.add(BiomeKeys.MANGROVE_SWAMP)
				.add(BiomeKeys.SWAMP);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_MUSHROOM)
				.add(BiomeKeys.MUSHROOM_FIELDS);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_UNDERGROUND)
				.addOptionalTag(ConventionalBiomeTags.IS_CAVE);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_MOUNTAIN)
				.addOptionalTag(BiomeTags.IS_MOUNTAIN);
	}

	private void generateOtherBiomeTypes() {
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_BADLANDS)
				.addOptionalTag(BiomeTags.IS_BADLANDS);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_CAVE)
				.add(BiomeKeys.DEEP_DARK)
				.add(BiomeKeys.DRIPSTONE_CAVES)
				.add(BiomeKeys.LUSH_CAVES);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_VOID)
				.add(BiomeKeys.THE_VOID);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_DEEP_OCEAN)
				.add(BiomeKeys.DEEP_FROZEN_OCEAN)
				.add(BiomeKeys.DEEP_LUKEWARM_OCEAN)
				.add(BiomeKeys.DEEP_COLD_OCEAN)
				.add(BiomeKeys.DEEP_OCEAN);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_SHALLOW_OCEAN)
				.add(BiomeKeys.OCEAN)
				.add(BiomeKeys.LUKEWARM_OCEAN)
				.add(BiomeKeys.FROZEN_OCEAN)
				.add(BiomeKeys.COLD_OCEAN)
				.add(BiomeKeys.WARM_OCEAN);
		getOrCreateTagBuilder(ConventionalBiomeTags.NO_DEFAULT_MONSTERS)
				.add(BiomeKeys.MUSHROOM_FIELDS)
				.add(BiomeKeys.DEEP_DARK);
	}

	private void generateClimateAndVegetationTags() {
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_COLD_OVERWORLD)
				.add(BiomeKeys.SNOWY_PLAINS)
				.add(BiomeKeys.SNOWY_SLOPES)
				.add(BiomeKeys.GROVE)
				.add(BiomeKeys.JAGGED_PEAKS)
				.add(BiomeKeys.TAIGA, BiomeKeys.SNOWY_TAIGA)
				.add(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA, BiomeKeys.OLD_GROWTH_PINE_TAIGA)
				.addOptionalTag(ConventionalBiomeTags.IS_ICY);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_COLD)
				.addTag(ConventionalBiomeTags.IS_COLD_OVERWORLD);

		getOrCreateTagBuilder(ConventionalBiomeTags.IS_TEMPERATE_OVERWORLD)
				.add(BiomeKeys.FOREST)
				.add(BiomeKeys.SUNFLOWER_PLAINS)
				.add(BiomeKeys.SWAMP)
				.add(BiomeKeys.STONY_SHORE)
				.add(BiomeKeys.DARK_FOREST)
				.add(BiomeKeys.WINDSWEPT_FOREST)
				.add(BiomeKeys.BIRCH_FOREST)
				.add(BiomeKeys.OLD_GROWTH_BIRCH_FOREST)
				.add(BiomeKeys.MEADOW)
				.add(BiomeKeys.PLAINS);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_TEMPERATE)
				.addTag(ConventionalBiomeTags.IS_TEMPERATE_OVERWORLD);

		getOrCreateTagBuilder(ConventionalBiomeTags.IS_HOT_OVERWORLD)
				.addOptionalTag(ConventionalBiomeTags.IS_JUNGLE)
				.addOptionalTag(ConventionalBiomeTags.IS_SAVANNA)
				.addOptionalTag(ConventionalBiomeTags.IS_DESERT)
				.addOptionalTag(ConventionalBiomeTags.IS_BADLANDS)
				.add(BiomeKeys.STONY_PEAKS)
				.addOptionalTag(ConventionalBiomeTags.IS_MUSHROOM)
				.addOptionalTag(ConventionalBiomeTags.IS_NETHER);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_HOT)
				.addTag(ConventionalBiomeTags.IS_HOT_OVERWORLD);

		getOrCreateTagBuilder(ConventionalBiomeTags.IS_WET_OVERWORLD)
				.addOptionalTag(ConventionalBiomeTags.IS_AQUATIC)
				.addOptionalTag(ConventionalBiomeTags.IS_SWAMP)
				.add(BiomeKeys.LUSH_CAVES)
				.addOptionalTag(ConventionalBiomeTags.IS_JUNGLE);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_WET)
				.addTag(ConventionalBiomeTags.IS_WET_OVERWORLD);

		getOrCreateTagBuilder(ConventionalBiomeTags.IS_DRY_OVERWORLD)
				.addOptionalTag(ConventionalBiomeTags.IS_NETHER)
				.addOptionalTag(ConventionalBiomeTags.IS_BADLANDS)
				.addOptionalTag(ConventionalBiomeTags.IS_DESERT)
				.addOptionalTag(ConventionalBiomeTags.IS_SAVANNA);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_DRY)
				.addTag(ConventionalBiomeTags.IS_DRY_OVERWORLD);

		getOrCreateTagBuilder(ConventionalBiomeTags.IS_VEGETATION_DENSE_OVERWORLD)
				.addOptionalTag(ConventionalBiomeTags.IS_JUNGLE)
				.add(BiomeKeys.DARK_FOREST);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_VEGETATION_DENSE)
				.addOptionalTag(ConventionalBiomeTags.IS_VEGETATION_DENSE_OVERWORLD);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_VEGETATION_SPARSE_OVERWORLD)
				.addOptionalTag(ConventionalBiomeTags.IS_SAVANNA)
				.addOptionalTag(ConventionalBiomeTags.IS_DESERT)
				.addOptionalTag(ConventionalBiomeTags.IS_DEAD)
				.addOptionalTag(ConventionalBiomeTags.IS_WASTELAND);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_VEGETATION_SPARSE)
				.addOptionalTag(ConventionalBiomeTags.IS_VEGETATION_SPARSE_OVERWORLD);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_CONIFEROUS_TREE)
				.add(BiomeKeys.GROVE)
				.addOptionalTag(ConventionalBiomeTags.IS_TAIGA);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_DECIDUOUS_TREE)
				.add(BiomeKeys.FOREST)
				.add(BiomeKeys.WINDSWEPT_FOREST)
				.add(BiomeKeys.FLOWER_FOREST)
				.add(BiomeKeys.BIRCH_FOREST)
				.add(BiomeKeys.DARK_FOREST)
				.add(BiomeKeys.OLD_GROWTH_BIRCH_FOREST);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_JUNGLE_TREE)
				.addOptionalTag(ConventionalBiomeTags.IS_JUNGLE);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_SAVANNA_TREE)
				.addOptionalTag(ConventionalBiomeTags.IS_SAVANNA);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_FLORAL)
				.add(BiomeKeys.SUNFLOWER_PLAINS)
				.add(BiomeKeys.MEADOW)
				.add(BiomeKeys.CHERRY_GROVE)
				.addOptionalTag(ConventionalBiomeTags.IS_FLOWER_FOREST);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_FLOWER_FOREST)
				.add(BiomeKeys.FLOWER_FOREST)
				.addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "flower_forests"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_OLD_GROWTH)
				.add(BiomeKeys.OLD_GROWTH_BIRCH_FOREST)
				.add(BiomeKeys.OLD_GROWTH_PINE_TAIGA)
				.add(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA);
	}

	private void generateTerrainDescriptorTags() {
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_MOUNTAIN_PEAK)
				.add(BiomeKeys.FROZEN_PEAKS)
				.add(BiomeKeys.JAGGED_PEAKS)
				.add(BiomeKeys.STONY_PEAKS);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_MOUNTAIN_SLOPE)
				.add(BiomeKeys.SNOWY_SLOPES);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_AQUATIC)
				.addOptionalTag(ConventionalBiomeTags.IS_OCEAN)
				.addOptionalTag(ConventionalBiomeTags.IS_RIVER);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_DEAD);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_WASTELAND);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_END_ISLAND);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_NETHER_FOREST)
				.add(BiomeKeys.WARPED_FOREST)
				.add(BiomeKeys.CRIMSON_FOREST);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_SNOWY_PLAINS)
				.add(BiomeKeys.SNOWY_PLAINS);
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_STONY_SHORES)
				.add(BiomeKeys.STONY_SHORE);
	}

	private void generateBackwardsCompatTags() {
		// Backwards compat with pre-1.21 tags. Done after so optional tag is last for better readability.
		// TODO: Remove backwards compat tag entries in 1.22

		getOrCreateTagBuilder(ConventionalBiomeTags.IS_NETHER).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "in_nether"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_THE_END).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "in_the_end"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_OVERWORLD).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "in_the_overworld"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_CAVE).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "caves"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_COLD_OVERWORLD).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "climate_cold"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_TEMPERATE_OVERWORLD).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "climate_temperate"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_HOT_OVERWORLD).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "climate_hot"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_WET_OVERWORLD).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "climate_wet"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_DRY_OVERWORLD).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "climate_dry"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_VEGETATION_DENSE_OVERWORLD).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "vegetation_dense"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_VEGETATION_SPARSE_OVERWORLD).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "vegetation_sparse"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_CONIFEROUS_TREE).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "tree_coniferous"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_DECIDUOUS_TREE).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "tree_deciduous"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_JUNGLE_TREE).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "tree_jungle"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_SAVANNA_TREE).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "tree_savanna"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_MOUNTAIN_PEAK).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "mountain_peak"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_MOUNTAIN_SLOPE).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "mountain_slope"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_END_ISLAND).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "end_islands"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_NETHER_FOREST).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "nether_forests"));
		getOrCreateTagBuilder(ConventionalBiomeTags.IS_FLOWER_FOREST).addOptionalTag(new Identifier(TagUtil.C_TAG_NAMESPACE, "flower_forests"));
	}
}
