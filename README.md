# File Compression with Huffman Encoding

This Java program implements file compression using Huffman encoding technique. It takes an input text file, compresses its content, and saves the compressed data to an output file.

## Usage

1. **Input File**: Create a text file named `input.txt` containing the text you want to compress.

2. **Compile**: Compile the `Compressor.java` file using a Java compiler.

    ```bash
    javac Compressor.java
    ```

3. **Run**: Execute the compiled program.

    ```bash
    java Compressor
    ```

4. **Output File**: Enter the desired name for the compressed file when prompted. The compressed file will be saved with the specified name.

## Details

- The program first reads the content of the input file (`input.txt`).
- It then constructs a Huffman tree based on the frequency of characters in the input text.
- The Huffman tree is used to encode each character in the input text with variable-length binary codes.
- The encoded data is written to the output file.
