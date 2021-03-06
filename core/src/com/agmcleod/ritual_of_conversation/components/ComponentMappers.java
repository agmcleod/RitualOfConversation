package com.agmcleod.ritual_of_conversation.components;

import com.badlogic.ashley.core.ComponentMapper;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class ComponentMappers {
    public static final ComponentMapper<DialogueOptionComponent> dialogueOptions = ComponentMapper.getFor(DialogueOptionComponent.class);
    public static final ComponentMapper<BoundingBoxComponent> collidable = ComponentMapper.getFor(BoundingBoxComponent.class);
    public static final ComponentMapper<InstructionStateComponent> instructionstate = ComponentMapper.getFor(InstructionStateComponent.class);
    public static final ComponentMapper<NpcStateComponent> npcState = ComponentMapper.getFor(NpcStateComponent.class);
    public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<TransformComponent> transformable = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<TextContentComponent> textContent = ComponentMapper.getFor(TextContentComponent.class);
}
