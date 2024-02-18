ALTER TABLE discord_guild
ADD COLUMN pesonal_welcome TEXT;

ALTER TABLE discord_guild
    ADD COLUMN guild_welcome TEXT;

ALTER TABLE discord_guild
    ADD COLUMN welcome_channel VARCHAR(255);