package common.versionupdate;

import java.io.File;

public interface DownLoadProgressListener {
	public void onTotalCount(int mount);
	public void onProgressChanged(int downloaded);
	public void onDownloadFailed();
	public void onDownloadFinished(File apkFile);
}
