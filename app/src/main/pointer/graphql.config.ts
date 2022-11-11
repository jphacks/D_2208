import { CodegenConfig } from "@graphql-codegen/cli";
import { join } from "path";

const config: CodegenConfig = {
  overwrite: true,
  schema: join(__dirname, "../resources/graphql/schema.graphqls"),
  documents: ["src/**/*.ts{,x}"],
  ignoreNoDocuments: true,
  generates: {
    "./src/gql/": {
      preset: "client",
      plugins: [],
    },
  },
};

export default config;
