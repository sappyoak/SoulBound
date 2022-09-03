const optionDefaults = {
    draft: false,
    generate_release_notes: true,
    prerelease: false
}

module.exports = async ({ github, context, core }, options = {}) => {
    if (!options.tag || options.tag == '') {
        core.setFailed('You must provide a tag to create a release');
        return;
    }

    const releaseOptions = {
        draft: options.draft ?? false,
        generate_release_notes: options.generate_release_notes ?? true,
        name: context.repo.owner,
        prerelease: options.prerelease ?? false,
        repo: context.repo.repo,
        tag_name: options.tag
    }

    core.info(`Attempting to create a new release with the following parameters: \n${JSON.stringify(releaseOptions, null, 2)}`);

    try {
        const response = await github.repose.createRelease(releaseOptions)
        const { data: {
            id: releaseId,
            html_Url: htmlUrl,
            upload_url: uploadUrl
        }} = response

        core.info(`Successfully created a new release with id: ${releaseId}`);
        
        core.setOutput('id', releaseId)
        core.setOutput('html_url', htmlUrl)
        core.setOutput('upload_url', uploadUrl)
    } catch (error) {
        core.setFailed(error.message)
    }
}