module.exports = {
  env: {
    browser: true,
    es2021: true,
    node: true,
  },
  extends: [
    "eslint:recommended",
    "plugin:import/recommended",
    "plugin:import/typescript",
    "plugin:@typescript-eslint/recommended",
    "prettier",
  ],
  overrides: [],
  parser: "@typescript-eslint/parser",
  parserOptions: {
    ecmaVersion: "latest",
    sourceType: "module",
  },
  plugins: ["@typescript-eslint"],
  settings: {
    "import/resolver": {
      typescript: {},
    },
  },
  rules: {
    // TypeScript ESLint が references をサポートしておらず、main プロセスの import alias を解決できないため
    // https://github.com/typescript-eslint/typescript-eslint/issues/2094
    "import/no-unresolved": "off",
    "import/order": [
      "error",
      {
        pathGroups: [
          {
            pattern: "@/**",
            group: "internal",
            position: "before",
          },
        ],
        groups: [
          ["builtin", "external"],
          ["parent"],
          ["internal"],
          ["index", "sibling"],
        ],
        "newlines-between": "always",
        alphabetize: {
          order: "asc",
          caseInsensitive: true,
        },
      },
    ],
    "no-restricted-imports": [
      "error",
      {
        patterns: ["../**", "./*/**"],
      },
    ],
  },
};
