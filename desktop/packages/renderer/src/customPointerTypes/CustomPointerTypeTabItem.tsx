import {
  Box,
  VStack,
  FormControl,
  FormLabel,
  Input,
  FormHelperText,
  Image,
  Center,
  Button,
  Icon,
  Text,
} from "@chakra-ui/react";
import type { CustomPointerType } from "@smartpointer-desktop/shared";
import { FC, FormEvent, useState } from "react";
import { useDropzone } from "react-dropzone";

import { useAutosave } from "./useAutosave";

import { updateCustomPointerType, removeCustomPointerType } from "#preload";

export const CustomPointerTypeTabItem: FC<{
  customPointerType: CustomPointerType;
}> = ({ customPointerType }) => {
  const [values, setValues] = useState<CustomPointerType>(customPointerType);

  const onChanges = (e: FormEvent<HTMLInputElement>) => {
    const { name, value } = e.currentTarget;
    setValues((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  useAutosave({
    data: values,
    onSave: (data) => {
      if (data !== customPointerType) {
        updateCustomPointerType(data);
      }
    },
  });

  const { getRootProps, getInputProps } = useDropzone({
    accept: {
      "image/jpeg": [],
      "image/png": [],
      "image/gif": [],
    },
    onDrop: (acceptedFiles) => {
      const file = acceptedFiles[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = () => {
          const result = reader.result;
          if (result) {
            console.log(result);
            setValues((prev) => ({ ...prev, image: result.toString() }));
          }
        };
        reader.readAsDataURL(file);
      }
    },
  });

  return (
    <VStack spacing="4">
      <FormControl>
        <FormLabel>カスタムポインター名</FormLabel>
        <Input value={values.name} onChange={onChanges} name="name" />
      </FormControl>
      <FormControl>
        <FormLabel>画像</FormLabel>
        <Box
          {...getRootProps()}
          w="full"
          bg="gray.100"
          p="4"
          rounded="md"
          border="dashed"
          borderColor="gray.300"
          color="gray.800"
          _hover={{
            bg: "gray.200",
          }}
          _focusVisible={{
            boxShadow: "outline",
            outline: "none",
          }}
        >
          <Center flexDir="column" gap="4">
            {values.image ? (
              <Image src={values.image} boxSize="40" objectFit="contain" />
            ) : (
              <Center boxSize="40">
                <VStack>
                  <Icon
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    strokeWidth={1.5}
                    stroke="currentColor"
                    boxSize="12"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      d="M18.364 18.364A9 9 0 005.636 5.636m12.728 12.728A9 9 0 015.636 5.636m12.728 12.728L5.636 5.636"
                    />
                  </Icon>

                  <Text textTransform="uppercase">no image</Text>
                </VStack>
              </Center>
            )}
            <input {...getInputProps()} />
            <FormHelperText>
              クリックして画像を選択するか、ここにドラッグ＆ドロップしてください
            </FormHelperText>
          </Center>
        </Box>
      </FormControl>
      <FormControl>
        <Button
          colorScheme="red"
          onClick={() => removeCustomPointerType(customPointerType)}
        >
          削除する
        </Button>
      </FormControl>
    </VStack>
  );
};
