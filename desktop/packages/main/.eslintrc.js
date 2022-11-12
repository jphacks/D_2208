module.exports = {
  extends: ["@smartpointer-desktop/eslintrc/index.js"],
  rules: {
    "import/no-restricted-paths": [
      "error",
      {
        zones: [
          {
            from: "./src/model/**/*",
            target: "./src/(controller|view)/**/*",
            message:
              "Model 層は Controller 層および View 層に依存してはいけません",
          },
          {
            from: "./src/view/**/*",
            target: "./src/model/**/*",
            message: "View 層は Model 層に依存してはいけません",
          },
          {
            from: "./src/controller/**/*",
            target: "./src/view/**/*",
            message: "Controller 層は View 層に依存してはいけません",
          },
        ],
      },
    ],
  },
};
