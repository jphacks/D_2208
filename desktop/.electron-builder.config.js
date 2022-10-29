/**
 * @type {import('electron-builder').Configuration}
 * @see https://www.electron.build/configuration/configuration
 */
const config = {
  directories: {
    output: "dist",
  },
  files: ["packages/**/dist/**"],
  extraResources: ["assets"],
};

module.exports = config;
