module.exports = async ({ context, core, github, require }, options = {}) => {
    const fs = require('fs')
    const glob = require('glob')
    const path = require('path')

    if (options.glob) {
        const files = glob.sync(options.file)
        if (files.length > 0) {
            for (const file of files) {
                const assetName = path.basename(file)
                await uploadFile(file, assetName)
            }
        } else {
            core.setFailed(`No files matching the glob pattern: ${options.glob}`)
        }
    } else {
        const assetName = path.basename(file)
        await uploadFile(file, assetName)
    }

    async function uploadFile(file, assetName) {
        try {
            const stat = fs.statSync(file);
            if (!stat.isFile()) {
                core.debug(`Skipping ${file} as it is not a file`)
                return
            }

            const fileSize = stat.size
            const fileBytes = fs.readFileSync(file)

            core.info(`Uploaded ${file} to ${assetName} in release ${options.tag}`);
            
            const uploadedAsset = await github.repos.uploadReleaseAsset({
                data: fileBytes,
                headers: {
                    'content-type': 'binary/octet-stream',
                    'content-length': file_size
                },
                name: assetName,
                url: options.uploadUrl
            })

            core.setOutput('browser_download_url', uploadedAsset.data.browser_download_url)
        } catch (error) {
            core.setFailed(`Could not upload asset ${file} to release: ${error.message}`)
        }
    }

}