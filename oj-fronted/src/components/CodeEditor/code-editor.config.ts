export enum LanguageEnum {
  Java = "java",
  Cpp = "cpp",
  Go = "go",
}

export const DefaultJavaContent = `class Main{
  public static void main(String[] args) {
    /* 请在此处编写代码  */
  }
}



`;

export const DefaultCppContent = `#include <iostream>

using namespace std;

int main(){
  /* 请在此处编写代码 */
  return 0;
}
`;

export const DefaultGoContent = `
import "fmt"

func main() {
  /* 请在此处编写代码 */
}

`;
