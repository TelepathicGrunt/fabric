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

package net.fabricmc.fabric.impl.tag.convention;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil;
import net.fabricmc.loader.api.FabricLoader;

public class ConventionLogWarnings implements ModInitializer {
	public static final String LOG_LEGACY_WARNING_MODE = System.getProperty("fabric-tag-conventions-v1.legacyTagWarning", LOG_WARNING_MODES.DEV_SHORT.name());
	public enum LOG_WARNING_MODES {
		SILENCED,
		DEV_SHORT,
		DEV_VERBOSE
	}

	public static final Logger LOGGER = LoggerFactory.getLogger(ConventionLogWarnings.class);

	// Old `c` tags that we migrated to a new tag under a new convention.
	// May also contain commonly used `c` tags that are not following convention.
	private static final Map<TagKey<?>, TagKey<?>> LEGACY_C_TAGS = Map.<TagKey<?>, TagKey<?>>ofEntries(
			// Old v1 tags that are discouraged
			createMapEntry(ConventionalBlockTags.MOVEMENT_RESTRICTED, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.RELOCATION_NOT_SUPPORTED),
			createMapEntry(ConventionalBlockTags.QUARTZ_ORES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.QUARTZ_ORES),
			createMapEntry(ConventionalBlockTags.WOODEN_BARRELS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.WOODEN_BARRELS),
			createMapEntry(ConventionalBlockTags.SANDSTONE_BLOCKS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.BLOCKS_SANDSTONE),
			createMapEntry(ConventionalBlockTags.SANDSTONE_STAIRS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.STAIRS_SANDSTONE),
			createMapEntry(ConventionalBlockTags.SANDSTONE_SLABS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.SLABS_SANDSTONE),
			createMapEntry(ConventionalBlockTags.RED_SANDSTONE_BLOCKS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.RED_BLOCKS_SANDSTONE),
			createMapEntry(ConventionalBlockTags.RED_SANDSTONE_STAIRS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.RED_STAIRS_SANDSTONE),
			createMapEntry(ConventionalBlockTags.RED_SANDSTONE_SLABS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.RED_SLABS_SANDSTONE),
			createMapEntry(ConventionalBlockTags.UNCOLORED_SANDSTONE_BLOCKS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.UNCOLORED_BLOCKS_SANDSTONE),
			createMapEntry(ConventionalBlockTags.UNCOLORED_SANDSTONE_STAIRS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.UNCOLORED_STAIRS_SANDSTONE),
			createMapEntry(ConventionalBlockTags.UNCOLORED_SANDSTONE_SLABS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.UNCOLORED_SLABS_SANDSTONE),

			createMapEntry(ConventionalItemTags.QUARTZ_ORES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.QUARTZ_ORES),
			createMapEntry(ConventionalItemTags.WOODEN_BARRELS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.WOODEN_BARRELS),
			createMapEntry(ConventionalItemTags.SANDSTONE_BLOCKS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.BLOCKS_SANDSTONE),
			createMapEntry(ConventionalItemTags.SANDSTONE_STAIRS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.STAIRS_SANDSTONE),
			createMapEntry(ConventionalItemTags.SANDSTONE_SLABS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.SLABS_SANDSTONE),
			createMapEntry(ConventionalItemTags.RED_SANDSTONE_BLOCKS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.RED_BLOCKS_SANDSTONE),
			createMapEntry(ConventionalItemTags.RED_SANDSTONE_STAIRS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.RED_STAIRS_SANDSTONE),
			createMapEntry(ConventionalItemTags.RED_SANDSTONE_SLABS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.RED_SLABS_SANDSTONE),
			createMapEntry(ConventionalItemTags.UNCOLORED_SANDSTONE_BLOCKS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.UNCOLORED_BLOCKS_SANDSTONE),
			createMapEntry(ConventionalItemTags.UNCOLORED_SANDSTONE_STAIRS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.UNCOLORED_STAIRS_SANDSTONE),
			createMapEntry(ConventionalItemTags.BLACK_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.BLACK_DYES),
			createMapEntry(ConventionalItemTags.BLUE_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.BLUE_DYES),
			createMapEntry(ConventionalItemTags.BROWN_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.BROWN_DYES),
			createMapEntry(ConventionalItemTags.GREEN_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.GREEN_DYES),
			createMapEntry(ConventionalItemTags.RED_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.RED_DYES),
			createMapEntry(ConventionalItemTags.WHITE_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.WHITE_DYES),
			createMapEntry(ConventionalItemTags.YELLOW_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.YELLOW_DYES),
			createMapEntry(ConventionalItemTags.LIGHT_BLUE_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.LIGHT_BLUE_DYES),
			createMapEntry(ConventionalItemTags.LIGHT_GRAY_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.LIGHT_GRAY_DYES),
			createMapEntry(ConventionalItemTags.LIME_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.LIME_DYES),
			createMapEntry(ConventionalItemTags.MAGENTA_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.MAGENTA_DYES),
			createMapEntry(ConventionalItemTags.ORANGE_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.ORANGE_DYES),
			createMapEntry(ConventionalItemTags.PINK_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.PINK_DYES),
			createMapEntry(ConventionalItemTags.CYAN_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.CYAN_DYES),
			createMapEntry(ConventionalItemTags.GRAY_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.GRAY_DYES),
			createMapEntry(ConventionalItemTags.PURPLE_DYES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.PURPLE_DYES),
			createMapEntry(ConventionalItemTags.RAW_IRON_ORES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.IRON_RAW_MATERIALS),
			createMapEntry(ConventionalItemTags.RAW_GOLD_ORES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.GOLD_RAW_MATERIALS),
			createMapEntry(ConventionalItemTags.DIAMONDS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.DIAMOND_GEMS),
			createMapEntry(ConventionalItemTags.LAPIS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.LAPIS_GEMS),
			createMapEntry(ConventionalItemTags.EMERALDS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.EMERALD_GEMS),
			createMapEntry(ConventionalItemTags.QUARTZ, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.QUARTZ_GEMS),
			createMapEntry(ConventionalItemTags.SHEARS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.SHEARS_TOOLS),
			createMapEntry(ConventionalItemTags.SPEARS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.SPEARS_TOOLS),
			createMapEntry(ConventionalItemTags.BOWS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.BOWS_TOOLS),
			createMapEntry(ConventionalItemTags.SHIELDS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.SHIELDS_TOOLS),

			createMapEntry(ConventionalBiomeTags.IN_NETHER, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.IS_NETHER),
			createMapEntry(ConventionalBiomeTags.IN_THE_END, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.IS_THE_END),
			createMapEntry(ConventionalBiomeTags.IN_OVERWORLD, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.IS_OVERWORLD),
			createMapEntry(ConventionalBiomeTags.CAVES, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.IS_CAVE),
			createMapEntry(ConventionalBiomeTags.CLIMATE_COLD, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.IS_COLD),
			createMapEntry(ConventionalBiomeTags.CLIMATE_TEMPERATE, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.IS_TEMPERATE),
			createMapEntry(ConventionalBiomeTags.CLIMATE_HOT, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.IS_HOT),
			createMapEntry(ConventionalBiomeTags.CLIMATE_WET, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.IS_WET),
			createMapEntry(ConventionalBiomeTags.CLIMATE_DRY, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.IS_DRY),
			createMapEntry(ConventionalBiomeTags.VEGETATION_DENSE, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.IS_VEGETATION_DENSE),
			createMapEntry(ConventionalBiomeTags.VEGETATION_SPARSE, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.IS_VEGETATION_SPARSE),
			createMapEntry(ConventionalBiomeTags.TREE_CONIFEROUS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.CONIFEROUS_IS_TREE),
			createMapEntry(ConventionalBiomeTags.TREE_DECIDUOUS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.DECIDUOUS_IS_TREE),
			createMapEntry(ConventionalBiomeTags.TREE_JUNGLE, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.JUNGLE_IS_TREE),
			createMapEntry(ConventionalBiomeTags.TREE_SAVANNA, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.SAVANNA_IS_TREE),
			createMapEntry(ConventionalBiomeTags.MOUNTAIN_PEAK, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.PEAK_IS_MOUNTAIN),
			createMapEntry(ConventionalBiomeTags.MOUNTAIN_SLOPE, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.SLOPE_IS_MOUNTAIN),
			createMapEntry(ConventionalBiomeTags.END_ISLANDS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.IS_END_ISLAND),
			createMapEntry(ConventionalBiomeTags.NETHER_FORESTS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.IS_NETHER_FOREST),
			createMapEntry(ConventionalBiomeTags.FLOWER_FORESTS, net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags.IS_FLOWER_FOREST),

			// Commonly used `c` tags that are using discouraged conventions. (Not plural or not folder form)
			createMapEntry(RegistryKeys.BLOCK, "barrel", net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.BARRELS),
			createMapEntry(RegistryKeys.BLOCK, "chest", net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.CHESTS),
			createMapEntry(RegistryKeys.BLOCK, "wooden_chests", net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.WOODEN_CHESTS),
			createMapEntry(RegistryKeys.BLOCK, "glass", net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.GLASS_BLOCKS),
			createMapEntry(RegistryKeys.BLOCK, "glass_pane", net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.GLASS_PANES),
			createMapEntry(RegistryKeys.BLOCK, "immobile", net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.RELOCATION_NOT_SUPPORTED),
			createMapEntry(RegistryKeys.BLOCK, "stone", net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.STONES),
			createMapEntry(RegistryKeys.BLOCK, "workbench", "workbenches"),

			createMapEntry(RegistryKeys.ITEM, "axes", ItemTags.AXES),
			createMapEntry(RegistryKeys.ITEM, "pickaxes", ItemTags.PICKAXES),
			createMapEntry(RegistryKeys.ITEM, "hoes", ItemTags.HOES),
			createMapEntry(RegistryKeys.ITEM, "shovels", ItemTags.SHOVELS),
			createMapEntry(RegistryKeys.ITEM, "swords", ItemTags.SWORDS),
			createMapEntry(createTagKeyUnderFabric(RegistryKeys.ITEM, "axes"), ItemTags.AXES),
			createMapEntry(createTagKeyUnderFabric(RegistryKeys.ITEM, "pickaxes"), ItemTags.PICKAXES),
			createMapEntry(createTagKeyUnderFabric(RegistryKeys.ITEM, "hoes"), ItemTags.HOES),
			createMapEntry(createTagKeyUnderFabric(RegistryKeys.ITEM, "shovels"), ItemTags.SHOVELS),
			createMapEntry(createTagKeyUnderFabric(RegistryKeys.ITEM, "swords"), ItemTags.SWORDS),
			createMapEntry(RegistryKeys.ITEM, "barrel", net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.BARRELS),
			createMapEntry(RegistryKeys.ITEM, "chest", net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.CHESTS),
			createMapEntry(RegistryKeys.ITEM, "glass", net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.GLASS_BLOCKS),
			createMapEntry(RegistryKeys.ITEM, "glass_pane", net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.GLASS_PANES),
			createMapEntry(RegistryKeys.ITEM, "glowstone_dusts", net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.GLOWSTONE_DUSTS),
			createMapEntry(RegistryKeys.ITEM, "redstone_dusts", net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.REDSTONE_DUSTS),
			createMapEntry(RegistryKeys.ITEM, "stone", net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.STONES),
			createMapEntry(RegistryKeys.ITEM, "string", net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.STRINGS),
			createMapEntry(RegistryKeys.ITEM, "wooden_rods", net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.WOODEN_RODS)

	);

	@Override
	public void onInitialize() {
		if (FabricLoader.getInstance().isDevelopmentEnvironment() && !LOG_LEGACY_WARNING_MODE.equalsIgnoreCase(LOG_WARNING_MODES.SILENCED.name())) {
			setupLegacyTagWarning();
		}
	}

	private static void setupLegacyTagWarning() {
		// Log tags that are still using legacy conventions under 'c' namespace
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			List<TagKey<?>> legacyTags = new ObjectArrayList<>();
			DynamicRegistryManager.Immutable dynamicRegistries = server.getRegistryManager();

			// We only care about vanilla registries
			dynamicRegistries.streamAllRegistries().forEach(registryEntry -> {
				if (registryEntry.key().getValue().getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
					registryEntry.value().streamTags().forEach(tagKey -> {
						// Grab legacy tags we migrated or discourage
						if (LEGACY_C_TAGS.containsKey(tagKey)) {
							legacyTags.add(tagKey);
						}
					});
				}
			});

			if (!legacyTags.isEmpty()) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("""
						\n	Dev warning - Legacy Tags detected. Please migrate your old `c` tags to our new `c` tags that follows better conventions!
							See classes under net.fabricmc.fabric.api.tag.convention.v2 package for all tags.

							NOTE: Many tags have been moved around or renamed. Some new ones were added so please review the new tags.
							And make sure you follow tag conventions for new tags! The convention is `c` with nouns generally being plural and adjectives being singular.
							You can disable this message by this system property to your runs: `-Dfabric-tag-conventions-v1.legacyTagWarning=SILENCED`.
							To see individual legacy tags found, set the system property to `-Dfabric-tag-conventions-v1.legacyTagWarning=DEV_VERBOSE` instead. Default is `DEV_SHORT`.
						""");

				// Print out all legacy tags when desired.
				boolean isConfigSetToVerbose = LOG_LEGACY_WARNING_MODE.equalsIgnoreCase(LOG_WARNING_MODES.DEV_VERBOSE.name());

				if (isConfigSetToVerbose) {
					stringBuilder.append("\nLegacy tags and their replacement:");

					for (TagKey<?> tagKey : legacyTags) {
						stringBuilder.append("\n     ").append(tagKey).append("  ->  ").append(LEGACY_C_TAGS.get(tagKey));
					}
				}

				LOGGER.warn(stringBuilder.toString());
			}
		});
	}

	private static <T> AbstractMap.SimpleEntry<TagKey<T>, TagKey<T>> createMapEntry(TagKey<T> tag1, TagKey<T> tag2) {
		return new AbstractMap.SimpleEntry<>(tag1, tag2);
	}

	private static <T> AbstractMap.SimpleEntry<TagKey<T>, TagKey<T>> createMapEntry(RegistryKey<Registry<T>> registryKey, String tagId1, TagKey<T> tag2) {
		return new AbstractMap.SimpleEntry<>(createTagKeyUnderC(registryKey, tagId1), tag2);
	}

	private static <T> AbstractMap.SimpleEntry<TagKey<T>, TagKey<T>> createMapEntry(RegistryKey<Registry<T>> registryKey, String tagId1, String tagId2) {
		return new AbstractMap.SimpleEntry<>(createTagKeyUnderC(registryKey, tagId1), createTagKeyUnderC(registryKey, tagId2));
	}

	private static <T> TagKey<T> createTagKeyUnderC(RegistryKey<Registry<T>> registryKey, String tagId) {
		return TagKey.of(registryKey, new Identifier(TagUtil.C_TAG_NAMESPACE, tagId));
	}

	private static <T> TagKey<T> createTagKeyUnderFabric(RegistryKey<Registry<T>> registryKey, String tagId) {
		return TagKey.of(registryKey, new Identifier(TagUtil.FABRIC_TAG_NAMESPACE, tagId));
	}
}
