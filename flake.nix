{
  description = "Minecraft modding flake";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils, ... }@inputs:
  flake-utils.lib.eachDefaultSystem (system:
    let
      pkgs = import nixpkgs {
        inherit system;
        config.allowUnfree = true;
      };
    in {
      devShells.default = pkgs.mkShell {
        buildInputs = with pkgs; [
          jetbrains.idea-ultimate
        ];

        LD_LIBRARY_PATH = nixpkgs.lib.makeLibraryPath [ pkgs.openal pkgs.glfw pkgs.libGL pkgs.flite ];

        JAVA_HOME = "${pkgs.jdk21}/lib/openjdk";
        JAVA_17_HOME = "${pkgs.jdk17}/lib/openjdk";
      };
    }
  );
}
