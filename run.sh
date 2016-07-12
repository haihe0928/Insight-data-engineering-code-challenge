#!/bin/bash
#
# The Insight Data Engineering Code Challenge 2016-07-11
# 
# Use java
#
# Shihai He


makedirs() {
    for dir in $@; do
        if [[ ! -e "${dir}" ]]; then
            echo "Making directory ${dir}" >&2
            mkdir "${dir}"
        fi
    done
}

main() {
    # Variables and configuration

    local dir_in='venmo_input'
    local dir_out='venmo_output'

    local gradle_cmd='./gradlew'
    local java_project_name='Venmo'
    local java_bin_path="./build/install/${java_project_name}/bin"
    local java_Venmo_cmd="${java_bin_path}/venmo"




    makedirs "${dir_in}" "${dir_out}"
    echo "See all the source in src/venmo/" >&2
    if [[ ! -x ${java_Venmo_cmd} ]]; then
        echo "Installing: ${java_Venmo_cmd}" >&2
        ${gradle_cmd} installDist
    fi

    ${java_Venmo_cmd} -o "${dir_out}" "${dir_in}"/*


}
main $@