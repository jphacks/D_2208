import {
  Box,
  Button,
  Center,
  Flex,
  FormControl,
  FormHelperText,
  FormLabel,
  Heading,
  HStack,
  Icon,
  Image,
  Input,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Spacer,
  StackDivider,
  Text,
  useDisclosure,
  VStack,
} from "@chakra-ui/react";
import { NoSymbolIcon, PlusIcon, TrashIcon } from "@heroicons/react/24/outline";
import type { CustomPointerType } from "@smartpointer-desktop/shared";
import { FC, useEffect, useMemo, useState } from "react";
import { useDropzone } from "react-dropzone";

import {
  addCustomPointerType,
  getCustomPointerTypes,
  onUpdateCustomPointerTypes,
  removeCustomPointerType,
} from "#preload";

const SelectPicture: FC<{
  value: File | undefined;
  onChange: (acceptedFiles: File[]) => void;
}> = ({ value, onChange }) => {
  const { getRootProps, getInputProps } = useDropzone({
    accept: {
      "image/jpeg": [],
      "image/png": [],
      "image/gif": [],
    },
    maxFiles: 1,
    maxSize: 5 * 1024 * 1024, // 5MB
    onDrop: onChange,
  });

  const url = useMemo(() => value && URL.createObjectURL(value), [value]);

  return (
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
        {url ? (
          <Image src={url} boxSize="40" objectFit="contain" />
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
                <NoSymbolIcon />
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
  );
};

export const App: FC = () => {
  const [customPointerTypes, setCustomPointerTypes] = useState<
    CustomPointerType[]
  >([]);

  const { isOpen, onOpen, onClose } = useDisclosure();

  useEffect(() => {
    getCustomPointerTypes().then(setCustomPointerTypes);
    onUpdateCustomPointerTypes(setCustomPointerTypes);
  }, []);

  const [newCustomPointerTypeLabel, setNewCustomPointerTypeLabel] =
    useState("");
  const [newCustomPointerTypeImage, setNewCustomPointerTypeImage] = useState<
    File | undefined
  >();

  const onCreate = () => {
    if (newCustomPointerTypeLabel && newCustomPointerTypeImage) {
      const reader = new FileReader();
      reader.onload = () => {
        const result = reader.result;
        if (result) {
          addCustomPointerType(newCustomPointerTypeLabel, result.toString());
          setNewCustomPointerTypeLabel("");
          setNewCustomPointerTypeImage(undefined);
        }
      };
      reader.readAsDataURL(newCustomPointerTypeImage);

      onClose();
    }
  };

  const onCancel = () => {
    setNewCustomPointerTypeLabel("");
    setNewCustomPointerTypeImage(undefined);
    onClose();
  };
  return (
    <VStack w="full" h="full" align="stretch" spacing="0" overflow="hidden">
      <Flex
        align="center"
        py="2"
        px="4"
        borderBottom="solid"
        borderBottomColor="gray.200"
        borderBottomWidth="thin"
      >
        <Heading fontSize="xl">カスタムポインターの設定</Heading>
        <Spacer />
        <Button
          colorScheme="green"
          leftIcon={<Icon as={PlusIcon} />}
          onClick={onOpen}
        >
          追加する
        </Button>
      </Flex>
      <VStack
        h="full"
        overflowY="scroll"
        flexShrink={0}
        flexGrow={1}
        align="stretch"
        divider={<StackDivider borderColor="gray.200" />}
        sx={{
          "&::-webkit-scrollbar": {
            width: "0.5rem",
            backgroundColor: "gray.200",
          },
          "&::-webkit-scrollbar-thumb": {
            backgroundColor: "gray.500",
            borderRadius: "0.25rem",
          },
        }}
      >
        {customPointerTypes.length === 0 && (
          <VStack align="center" py="4" color="gray.500" spacing="4">
            <Text>カスタムポインターがありません</Text>
            <Text>「追加する」ボタンから追加してください</Text>
          </VStack>
        )}
        {customPointerTypes.map(({ id, label, content: url }) => (
          <HStack key={id} p="4" spacing="3" align="center">
            <Image src={url} boxSize="12" objectFit="contain" />
            <Text textAlign="left" flexGrow={1}>
              {label}
            </Text>
            <Button
              colorScheme="red"
              leftIcon={<Icon as={TrashIcon} />}
              onClick={() => removeCustomPointerType(id)}
            >
              削除する
            </Button>
          </HStack>
        ))}
      </VStack>
      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>新規カスタムポインター作成</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <FormControl>
              <FormLabel>カスタムポインター名</FormLabel>
              <Input
                value={newCustomPointerTypeLabel}
                onChange={(event) =>
                  setNewCustomPointerTypeLabel(event.currentTarget.value)
                }
                name="name"
              />
            </FormControl>
            <FormControl>
              <FormLabel>画像</FormLabel>
              <SelectPicture
                value={newCustomPointerTypeImage}
                onChange={(acceptedFiles) =>
                  setNewCustomPointerTypeImage(acceptedFiles[0])
                }
              />
            </FormControl>
          </ModalBody>

          <ModalFooter>
            <HStack spacing="4">
              <Button
                colorScheme="green"
                disabled={
                  !newCustomPointerTypeLabel || !newCustomPointerTypeImage
                }
                onClick={onCreate}
              >
                作成する
              </Button>
              <Button variant="ghost" onClick={onCancel}>
                キャンセル
              </Button>
            </HStack>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </VStack>
  );
};
