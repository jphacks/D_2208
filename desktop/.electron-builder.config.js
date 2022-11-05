/**
 * @type {import('electron-builder').Configuration}
 * @see https://www.electron.build/configuration/configuration
 */
const config = {
  directories: {
    output: "dist",
    buildResources: "buildResources",
  },
  files: ["packages/**/dist/**"],
  extraResources: ["assets"],
};

module.exports = config;
